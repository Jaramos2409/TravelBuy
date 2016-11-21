package com.jaramos2409.travelbuy.database;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.jaramos2409.travelbuy.datamodels.Shop;

/**
 * Created by EVA Unit 02 on 11/20/2016.
 */
public class DBTask extends
        AsyncTask<String, Void, Void> {

    private static final String LOG_TAG = DBTask.class.getSimpleName();


    private Context mContext;
    private ProgressDialog mProgressDialog;

    public DBTask(Context context) {
        mContext = context;
    }

    @Override
    protected void onPreExecute() {
        mProgressDialog = new ProgressDialog(mContext);
        mProgressDialog.setMax(100);
        mProgressDialog.setTitle("Account info is being loaded.");
        mProgressDialog.setMessage("Loading...");
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.show();
        mProgressDialog.setCancelable(false);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.show();
    }

    @Override
    protected Void doInBackground(String... params) {

        if (params[0].equals(DBTypes.GET_USER_INFO)) {
           // Shop.setCurrentShopInfo(DBQueryHandler.getCurrentShopInfo());
            Log.d(LOG_TAG, "Shop Info Loaded");
        } else if (params[0].equals(DBTypes.INSERT_NEW_USER)) {
            //DBQueryHandler.insertNewUser(params[1], params[2], params[3], mContext);
            Log.d(LOG_TAG, "Shop Inserted");
        } else if (params[0].equals(DBTypes.LOAD_SHOP)) {
            Shop.setCurrentShopInfo(DBQueryHandler.loadShopInfo());
            Shop.setOriginalShopInfo(Shop.getCurrentShopInfo());
        } else {
            Log.d(LOG_TAG, "Task Malfunction");
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }
}
