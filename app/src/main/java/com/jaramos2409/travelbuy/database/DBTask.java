package com.jaramos2409.travelbuy.database;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

//import com.jaramos2409.travelbuy.ShopItemActivity;
import com.jaramos2409.travelbuy.datamodels.Shop;
import com.jaramos2409.travelbuy.datamodels.ShopItem;

import java.util.ArrayList;

/**
 * Created by EVA Unit 02 on 11/20/2016.
 */
public class DBTask extends
        AsyncTask<String, Void, Void> {

    private ShopItem mShopItem;
    private ArrayList<ShopItem> mShopItemsList;

    private static final String LOG_TAG = DBTask.class.getSimpleName();

    private Context mContext;
    private ProgressDialog mProgressDialog;

    public DBTask(Context context) {
        mContext = context;
        mShopItem = new ShopItem();
        mShopItemsList = new ArrayList<>();
    }

    public DBTask() {
        super();
        mShopItem = new ShopItem();
        mShopItemsList = new ArrayList<>();
    }

    public DBTask(Context context, ShopItem shopItem) {
        super();
        mContext = context;
        mShopItem = shopItem;
        mShopItemsList = new ArrayList<>();
    }

    @Override
    protected void onPreExecute() {
        mProgressDialog = new ProgressDialog(mContext);
        mProgressDialog.setMax(100);
        mProgressDialog.setTitle("");
        mProgressDialog.setMessage("Loading...");
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.show();
    }

    @Override
    protected Void doInBackground(String... params) {

        if (params[0].equals(DBTypes.LOAD_SHOP)) {
            Shop.setCurrentShopInfo(DBQueryHandler.loadShopInfo());
            Shop.setOriginalShopInfo(Shop.getCurrentShopInfo());
        } else if (params[0].equals(DBTypes.INSERT_ITEM)) {
            DBQueryHandler.insertShopItem(mShopItem, mContext);
        } else if (params[0].equals(DBTypes.LOAD_ITEMS_LIST)) {
            mShopItemsList = DBQueryHandler.loadShopItems(mContext);
        } else {
            Log.d(LOG_TAG, "Task Malfunction");
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void result) {

        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    public ShopItem getShopItem() {
        return mShopItem;
    }

    public void setShopItem(ShopItem shopItem) {
        mShopItem = shopItem;
    }

    public ArrayList<ShopItem> getShopItemsList() {
        return mShopItemsList;
    }

    public void setShopItemsList(ArrayList<ShopItem> shopItemsList) {
        mShopItemsList = shopItemsList;
    }

    public ProgressDialog getProgressDialog() {
        return mProgressDialog;
    }

    public void setProgressDialog(ProgressDialog progressDialog) {
        mProgressDialog = progressDialog;
    }
}
