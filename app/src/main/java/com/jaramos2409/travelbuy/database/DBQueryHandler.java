package com.jaramos2409.travelbuy.database;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.mobile.AWSMobileClient;
import com.amazonaws.mobile.user.signin.CognitoUserPoolsSignInProvider;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserPool;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBQueryExpression;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.PaginatedQueryList;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.models.nosql.ShopItemsDO;
import com.amazonaws.models.nosql.ShopsDO;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;
import com.amazonaws.services.dynamodbv2.model.QueryRequest;
import com.amazonaws.services.dynamodbv2.model.QueryResult;
import com.amazonaws.services.dynamodbv2.model.ReturnConsumedCapacity;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;
import com.jaramos2409.travelbuy.datamodels.Shop;
import com.jaramos2409.travelbuy.datamodels.ShopItem;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.amazonaws.mobile.AWSConfiguration.AMAZON_S3_USER_FILES_BUCKET;

/**
 * Created by EVA Unit 02 on 11/18/2016.
 */
public class DBQueryHandler {
    private final static String LOG_TAG = DBQueryHandler.class.getSimpleName();
    private final static String SHOP_ITEMS_TABLE_NAME = "travelbuy-mobilehub-417374176-shopItems";

    public static Shop loadShopInfo()
    {
        Shop shop = new Shop();

        // Fetch the default configured DynamoDB ObjectMapper
        final DynamoDBMapper dynamoDBMapper = AWSMobileClient.defaultMobileClient().getDynamoDBMapper();

        ShopsDO shopInfo = dynamoDBMapper.load(ShopsDO.class,
                AWSMobileClient.defaultMobileClient().getIdentityManager().getCachedUserID());

        if(shopInfo != null) {
            shop.setUserId(shopInfo.getUserId());
            shop.setEmail(shopInfo.getEmail());
            shop.setUsername(shopInfo.getUsername());
            shop.setShopId(shopInfo.getShopId());
            shop.setIsSelling(shopInfo.getIsSelling());
        } else {
            UserAttributes userAttributes = new UserAttributes();

            CognitoUserPool userPool = CognitoUserPoolsSignInProvider.getPool();
            CognitoUser user = userPool.getCurrentUser();
            user.getDetails(userAttributes);

            String userId = AWSMobileClient.defaultMobileClient().getIdentityManager().getCachedUserID();
            shop.setUserId(userId);
            shop.setEmail(userAttributes.getAttribute("email"));
            shop.setUsername(userAttributes.getAttribute("preferred_username"));
            shop.setShopId("shop" + UUID.randomUUID());
            shop.setIsSelling(true);
            shop.setIsOpen(false);

            shopInfo = new ShopsDO();
            shopInfo.setUserId(shop.getUserId());
            shopInfo.setEmail(shop.getEmail());
            shopInfo.setShopId(shop.getShopId());
            shopInfo.setUsername(shop.getUsername());
            shopInfo.setIsSelling(shop.isSelling());
            shopInfo.setIsOpen(shop.isOpen());
            shopInfo.setLatitude(0.0);
            shopInfo.setLongitude(0.0);

            AmazonClientException lastException = null;

            try {
                dynamoDBMapper.save(shopInfo);
            } catch (final AmazonClientException ex) {
                Log.e(LOG_TAG, "Failed saving item : " + ex.getMessage(), ex);
                lastException = ex;
            }
        }

        return shop;
    }

    public static void insertShopItem(ShopItem shopItem, Context context)
    {
        // Fetch the default configured DynamoDB ObjectMapper
        final DynamoDBMapper dynamoDBMapper = AWSMobileClient.defaultMobileClient().getDynamoDBMapper();

        ShopItemsDO shopItemInfo = new ShopItemsDO();

        shopItemInfo.setShopId(shopItem.getShopId());
        shopItemInfo.setCategory(shopItem.getCategory());
        shopItemInfo.setDescription(shopItem.getDescription());

        if (shopItem.getItemId().isEmpty()) {
            shopItemInfo.setItemId("item" + UUID.randomUUID());
        } else {
            shopItemInfo.setItemId(shopItem.getItemId());
        }

        shopItemInfo.setName(shopItem.getName());
        shopItemInfo.setPrice(shopItem.getPrice());

        String filePath = shopItem.getItemPhotoPath();

        shopItemInfo.setItemImage(dynamoDBMapper.createS3Link(AMAZON_S3_USER_FILES_BUCKET,
                "item_photos/"+ shopItemInfo.getItemId() + filePath.substring(filePath.lastIndexOf("."))));

        AmazonClientException lastException = null;

        try {
            dynamoDBMapper.save(shopItemInfo);
        } catch (final AmazonClientException ex) {
            Log.e(LOG_TAG, "Failed saving item : " + ex.getMessage(), ex);
            lastException = ex;
        }

        TransferUtility transferUtility = new TransferUtility(shopItemInfo.getItemImage().getAmazonS3Client(), context);
        TransferObserver transfer = transferUtility.upload(AMAZON_S3_USER_FILES_BUCKET,
                shopItemInfo.getItemImage().getKey(), new File(shopItem.getItemPhotoPath()));
        transfer.setTransferListener(new TransferListener() {
            @Override
            public void onStateChanged(int id, TransferState state) {

            }

            @Override
            public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {

            }

            @Override
            public void onError(int id, Exception ex) {

            }
        });

        while (true) {
            if (TransferState.COMPLETED.equals(transfer.getState())
                    || TransferState.FAILED.equals(transfer.getState())) {
                break;
            }
        }

    }

    public static ArrayList<ShopItem> loadShopItems(Context context)
    {
        ArrayList<ShopItem> shopItems = new ArrayList<>();

        // Fetch the default configured DynamoDB ObjectMapper
        final DynamoDBMapper dynamoDBMapper = AWSMobileClient.defaultMobileClient().getDynamoDBMapper();

        ShopItemsDO shopItemsDO = new ShopItemsDO();
        shopItemsDO.setShopId(Shop.getCurrentShopInfo().getShopId());

        DynamoDBQueryExpression<ShopItemsDO> queryExpression = new DynamoDBQueryExpression<ShopItemsDO>()
                .withHashKeyValues(shopItemsDO)
                .withConsistentRead(false);

        PaginatedQueryList<ShopItemsDO> result = dynamoDBMapper.query(ShopItemsDO.class, queryExpression);


        for (ShopItemsDO shopItemDO: result) {
            String key = shopItemDO.getItemImage().getKey();

            String imagePath = Environment.getExternalStorageDirectory().toString() + "/"
                    + shopItemDO.getItemId() + key.substring(key.lastIndexOf("."));

            File imageFile = new File(imagePath);
            try {
                Log.d(LOG_TAG, "Was new file created?: " + imageFile.createNewFile());
            } catch (IOException e) {
                e.printStackTrace();
            }

            TransferUtility transferUtility = new TransferUtility(shopItemDO.getItemImage().getAmazonS3Client(), context);

            TransferObserver transfer = transferUtility.download(AMAZON_S3_USER_FILES_BUCKET,
                    key, imageFile);

            transfer.setTransferListener(new TransferListener() {
                @Override
                public void onStateChanged(int id, TransferState state) {

                }

                @Override
                public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {

                }

                @Override
                public void onError(int id, Exception ex) {

                }
            });

            while (true) {
                if (TransferState.COMPLETED.equals(transfer.getState())
                        || TransferState.FAILED.equals(transfer.getState())) {
                    break;
                }
            }

            shopItems.add(new ShopItem(shopItemDO.getName(), shopItemDO.getPrice(), shopItemDO.getCategory(),
                    shopItemDO.getDescription(), shopItemDO.getItemId(), shopItemDO.getShopId(), imagePath));
        }

        return shopItems;
    }

    public static void deleteItem(ShopItem shopItem, Context context) {
        // Fetch the default configured DynamoDB ObjectMapper
        final DynamoDBMapper dynamoDBMapper = AWSMobileClient.defaultMobileClient().getDynamoDBMapper();

        ShopItemsDO shopItemInfo = new ShopItemsDO();
        String filePath = shopItem.getItemPhotoPath();
        shopItemInfo.setItemImage(dynamoDBMapper.createS3Link(AMAZON_S3_USER_FILES_BUCKET,
                "item_photos/" + shopItemInfo.getItemId() + filePath.substring(filePath.lastIndexOf("."))));
        shopItemInfo.setCategory(shopItem.getCategory());
        shopItemInfo.setDescription(shopItem.getDescription());
        shopItemInfo.setItemId(shopItem.getItemId());
        shopItemInfo.setName(shopItem.getName());
        shopItemInfo.setPrice(shopItem.getPrice());
        shopItemInfo.setShopId(shopItem.getShopId());

        AmazonClientException lastException = null;

        try {
            dynamoDBMapper.delete(shopItemInfo);
        } catch (final AmazonClientException ex) {
            Log.e(LOG_TAG, "Failed deleting item : " + ex.getMessage(), ex);
            lastException = ex;
        }

        try {
            shopItemInfo.getItemImage().getAmazonS3Client()
                    .deleteObject(AMAZON_S3_USER_FILES_BUCKET, shopItemInfo.getItemImage().getKey());
        } catch (final AmazonServiceException ex) {
            Log.e(LOG_TAG, "Amazon Error: " + ex.getMessage(), ex);
            lastException = ex;
        }
    }


    public static ArrayList<ShopItem> searchShopItems(Context context, String query, String category)
    {
        ArrayList<ShopItem> shopItems = new ArrayList<>();

        // Fetch the default configured DynamoDB ObjectMapper
        final DynamoDBMapper dynamoDBMapper = AWSMobileClient.defaultMobileClient().getDynamoDBMapper();

        final AmazonDynamoDB dynamoDB = AWSMobileClient.defaultMobileClient().getDynamoDBClient();

        ScanRequest scanRequest = new ScanRequest();
        Map<String,AttributeValue> expressionAttributeValues = new HashMap<>();
        Map<String, String> expressionAttributeNames = new HashMap<>();


        scanRequest.withTableName(SHOP_ITEMS_TABLE_NAME);
        scanRequest.withIndexName("Name");

        boolean isQueryEmpty = query.isEmpty();
        boolean isCategoryAll = category.equals("All");

        if(!isQueryEmpty && isCategoryAll) {
            scanRequest.withFilterExpression("begins_with (#N, :n)");
            expressionAttributeValues.put(":n", new AttributeValue(query));
            expressionAttributeNames.put("#N", "name");
            scanRequest.withExpressionAttributeValues(expressionAttributeValues);
            scanRequest.withExpressionAttributeNames(expressionAttributeNames);
        } else if (!isQueryEmpty) {
            scanRequest.withFilterExpression("begins_with (#N, :n) AND category = :c");
            expressionAttributeValues.put(":n", new AttributeValue(query));
            expressionAttributeNames.put("#N", "name");
            expressionAttributeValues.put(":c", new AttributeValue(category));
            scanRequest.withExpressionAttributeValues(expressionAttributeValues);
            scanRequest.withExpressionAttributeNames(expressionAttributeNames);
        } else if (!isCategoryAll) {
            scanRequest.withFilterExpression("category = :c");
            expressionAttributeValues.put(":c", new AttributeValue(category));
            scanRequest.withExpressionAttributeValues(expressionAttributeValues);
        } else {
            return shopItems;
        }

        scanRequest.withReturnConsumedCapacity(ReturnConsumedCapacity.TOTAL);

        ScanResult result = dynamoDB.scan(scanRequest);

        result.getItems();

        List<ShopItemsDO> mappedItems = new ArrayList<>();

        for(Map<String, AttributeValue> item : result.getItems()) {
            ShopItemsDO shopItemDO = dynamoDBMapper.marshallIntoObject(ShopItemsDO.class, item);
            mappedItems.add(shopItemDO);
        }

        for (ShopItemsDO shopItemDO: mappedItems) {
            String key = shopItemDO.getItemImage().getKey();

            String imagePath = Environment.getExternalStorageDirectory().toString() + "/"
                    + shopItemDO.getItemId() + key.substring(key.lastIndexOf("."));

            File imageFile = new File(imagePath);
            try {
                Log.d(LOG_TAG, "Was new file created?: " + imageFile.createNewFile());
            } catch (IOException e) {
                e.printStackTrace();
            }

            TransferUtility transferUtility = new TransferUtility(shopItemDO.getItemImage().getAmazonS3Client(), context);

            TransferObserver transfer = transferUtility.download(AMAZON_S3_USER_FILES_BUCKET,
                    key, imageFile);

            transfer.setTransferListener(new TransferListener() {
                @Override
                public void onStateChanged(int id, TransferState state) {

                }

                @Override
                public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {

                }

                @Override
                public void onError(int id, Exception ex) {

                }
            });

            while (true) {
                if (TransferState.COMPLETED.equals(transfer.getState())
                        || TransferState.FAILED.equals(transfer.getState())) {
                    break;
                }
            }

            shopItems.add(new ShopItem(shopItemDO.getName(), shopItemDO.getPrice(), shopItemDO.getCategory(),
                    shopItemDO.getDescription(), shopItemDO.getItemId(), shopItemDO.getShopId(), imagePath));
        }

        return shopItems;
    }
}
