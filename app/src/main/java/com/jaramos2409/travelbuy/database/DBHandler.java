package com.jaramos2409.travelbuy.database;

import android.util.Log;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.mobile.AWSMobileClient;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.models.nosql.UsersDO;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.model.DescribeTableRequest;
import com.amazonaws.services.dynamodbv2.model.DescribeTableResult;
import com.amazonaws.services.dynamodbv2.model.ResourceNotFoundException;

import java.util.UUID;

/**
 * Created by EVA Unit 02 on 11/18/2016.
 */
public class DBHandler {
    private final static String LOG_TAG = DBHandler.class.getSimpleName();

    public static void insertNewUser(String email, String username) {
        // Fetch the default configured DynamoDB ObjectMapper
        final DynamoDBMapper dynamoDBMapper = AWSMobileClient.defaultMobileClient().getDynamoDBMapper();
        final UsersDO newUserEntry = new UsersDO(); // Initialize the Notes Object

        // The userId has to be set to user's Cognito Identity Id for private / protected tables.
        // User's Cognito Identity Id can be fetched by using:
        // AWSMobileClient.defaultMobileClient().getIdentityManager().getCachedUserID()
        newUserEntry.setUserId(AWSMobileClient.defaultMobileClient().getIdentityManager().getCachedUserID());
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

}
