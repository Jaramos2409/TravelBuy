package com.jaramos2409.travelbuy.database;

import android.util.Log;

import com.amazonaws.AmazonClientException;
import com.amazonaws.mobile.AWSMobileClient;
import com.amazonaws.mobile.user.signin.CognitoUserPoolsSignInProvider;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserPool;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.models.nosql.ShopsDO;
import com.jaramos2409.travelbuy.datamodels.Shop;

import java.util.UUID;

/**
 * Created by EVA Unit 02 on 11/18/2016.
 */
public class DBQueryHandler {
    private final static String LOG_TAG = DBQueryHandler.class.getSimpleName();

    /*public static void insertNewUser(String email, String username, String userId, Context context) {
        // Fetch the default configured DynamoDB ObjectMapper
        final DynamoDBMapper dynamoDBMapper = AWSMobileClient.defaultMobileClient().getDynamoDBMapper();
        final UsersDO newUserEntry = new UsersDO(); // Initialize the Notes Object

        // The userId has to be set to user's Cognito Identity Id for private / protected tables.
        // Shop's Cognito Identity Id can be fetched by using:
        // AWSMobileClient.defaultMobileClient().getIdentityManager().getCachedUserID()
        newUserEntry.setUserId(userId);
        newUserEntry.setShopId("shop" + UUID.randomUUID());
        newUserEntry.setUsername(username);
        newUserEntry.setEmail(email);
        newUserEntry.setIsSelling(false);

        AmazonClientException lastException = null;

        try {
            dynamoDBMapper.save(newUserEntry);
        } catch (final AmazonClientException ex) {
            Log.e(LOG_TAG, "Failed saving item : " + ex.getMessage(), ex);
            lastException = ex;
        }


    }

    public static Shop checkIfUserExist()
    {
        Shop user = new Shop();

        // Fetch the default configured DynamoDB ObjectMapper
        final DynamoDBMapper dynamoDBMapper = AWSMobileClient.defaultMobileClient().getDynamoDBMapper();

        UsersDO usersInfo = dynamoDBMapper.load(UsersDO.class,
                AWSMobileClient.defaultMobileClient().getIdentityManager().getCachedUserID());

        if(usersInfo != null) {
            user.setUserId(usersInfo.getUserId());
            user.setEmail(usersInfo.getEmail());
            user.setUsername(usersInfo.getUsername());
            user.setShopId(usersInfo.getShopId());
            user.setIsSelling(usersInfo.getIsSelling());
        }

        return user;
    }

    public static Shop getCurrentShopInfo() {
        Shop user = new Shop();

        // Fetch the default configured DynamoDB ObjectMapper
        final DynamoDBMapper dynamoDBMapper = AWSMobileClient.defaultMobileClient().getDynamoDBMapper();

        UsersDO usersInfo = dynamoDBMapper.load(UsersDO.class,
                AWSMobileClient.defaultMobileClient().getIdentityManager().getCachedUserID());

        user.setUserId(usersInfo.getUserId());
        user.setEmail(usersInfo.getEmail());
        user.setUsername(usersInfo.getUsername());
        user.setShopId(usersInfo.getShopId());
        user.setIsSelling(usersInfo.getIsSelling());

        return user;
    }*/


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




}
