package com.jaramos2409.travelbuy.datamodels;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by EVA Unit 02 on 11/19/2016.
 */
public class Shop  {

    private static Shop sCurrentShopInfo = new Shop();
    public static Shop getCurrentShopInfo() {
        return sCurrentShopInfo;
    }
    public static void setCurrentShopInfo(Shop currentShopInfo) {
        Shop.sCurrentShopInfo = currentShopInfo;
    }

    private static Shop sOriginalShopInfo = new Shop();
    public static Shop getOriginalShopInfo() {return sOriginalShopInfo;}
    public static void setOriginalShopInfo(Shop originalShopInfo) {
        Shop.sOriginalShopInfo = originalShopInfo;
    }

    private String mUserId;
    private String mShopId;
    private String mEmail;
    private String mUsername;
    private boolean mIsSelling;
    private boolean mIsOpen;


    public Shop(String userId, String email, String username, boolean isSelling, String shopId, boolean isOpen) {
        mUserId = userId;
        mEmail = email;
        mUsername = username;
        mIsSelling = isSelling;
        mShopId = shopId;
        mIsOpen = isOpen;
    }

    public Shop() {
        mUserId = "";
        mEmail = "";
        mUsername = "";
        mIsSelling = false;
        mShopId = "";
        mIsOpen = false;
    }

    public String getUserId() {
        return mUserId;
    }

    public void setUserId(String userId) {
        this.mUserId = userId;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String email) {
        this.mEmail = email;
    }

    public String getUsername() {
        return mUsername;
    }

    public void setUsername(String username) {
        this.mUsername = username;
    }

    public boolean isSelling() {
        return mIsSelling;
    }

    public void setIsSelling(boolean isSelling) {
        this.mIsSelling = isSelling;
    }

    public String getShopId() {
        return mShopId;
    }

    public void setShopId(String shopId) {
        this.mShopId = shopId;
    }

    public boolean isOpen() {
        return mIsOpen;
    }

    public void setIsOpen(boolean isOpen) {
        mIsOpen = isOpen;
    }

    @Override
    public String toString() {
        return mUsername + "\n" +
                mEmail + "\n" +
                mShopId + "\n" +
                mIsSelling + "\n";
    }
}
