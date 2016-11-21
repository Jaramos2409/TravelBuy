package com.jaramos2409.travelbuy.database;

import android.util.Log;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.GetDetailsHandler;

import java.util.Map;

/**
 * Created by EVA Unit 02 on 11/20/2016.
 */
public class UserAttributes implements GetDetailsHandler {
    private final static String LOG_TAG = UserAttributes.class.getSimpleName();

    Map<String, String> attributes;

    @Override
    public void onSuccess(final CognitoUserDetails list) {
        // Successfully retrieved user details
        attributes = list.getAttributes().getAttributes();
        Log.d(LOG_TAG, "Attributes Found");
    }

    @Override
    public void onFailure(final Exception exception) {
        // Failed to retrieve the user details, probe exception for the cause
    }

    public String getAttribute(String key) {
        return attributes.get(key);
    }
}
