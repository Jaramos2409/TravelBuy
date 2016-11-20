//
// Copyright 2016 Amazon.com, Inc. or its affiliates (Amazon). All Rights Reserved.
//
// Code generated by AWS Mobile Hub. Amazon gives unlimited permission to 
// copy, distribute and modify it.
//
// Source code generated from template: aws-my-sample-app-android v0.10
//
package com.jaramos2409.travelbuy.login;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.amazonaws.mobile.user.signin.CognitoUserPoolsSignInProvider;
import com.jaramos2409.travelbuy.R;
import com.jaramos2409.travelbuy.database.DBHandler;
import com.jaramos2409.travelbuy.util.ViewHelper;

/**
 * Activity to prompt for account sign up information.
 */
public class SignUpActivity extends AppCompatActivity {
    /** Log tag. */
    private static final String LOG_TAG = SignUpActivity.class.getSimpleName();

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
    }

    private void dismissProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    @Override
    protected void onDestroy() {
        dismissProgressDialog();
        super.onDestroy();
    }

    /**
     * Retrieve input and return to caller.
     * @param view the Android View
     */
    public void signUp(final View view) {
        final String username = ViewHelper.getStringValue(this, R.id.signup_username);
        final String password = ViewHelper.getStringValue(this, R.id.signup_password);
        final String givenName = ViewHelper.getStringValue(this, R.id.signup_given_name);
        final String email = ViewHelper.getStringValue(this, R.id.signup_email);

        Log.d(LOG_TAG, "username = " + username);
        Log.d(LOG_TAG, "given_name = " + givenName);
        Log.d(LOG_TAG, "email = " + email);

        new InsertNewUserTask(this).execute(email, username);

        final Intent intent = new Intent();
        intent.putExtra(CognitoUserPoolsSignInProvider.AttributeKeys.USERNAME, username);
        intent.putExtra(CognitoUserPoolsSignInProvider.AttributeKeys.PASSWORD, password);
        intent.putExtra(CognitoUserPoolsSignInProvider.AttributeKeys.GIVEN_NAME, givenName);
        intent.putExtra(CognitoUserPoolsSignInProvider.AttributeKeys.EMAIL_ADDRESS, email);

        setResult(RESULT_OK, intent);

        finish();
    }


    private class InsertNewUserTask extends
            AsyncTask<String, Void, Void> {

        Context context;

        public InsertNewUserTask(Context context)
        {
            super();
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(context);
            progressDialog.setMax(100);
            progressDialog.setTitle("Account is being created!");
            progressDialog.setMessage("Loading...");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.show();
            progressDialog.setCancelable(false);
            progressDialog.setIndeterminate(true);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(String... params) {
            DBHandler.insertNewUser(params[0], params[1]);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            if (SignUpActivity.this.isDestroyed()) {
                return;
            }

            if (progressDialog != null) {
                progressDialog.dismiss();
            }
        }
    }

}
