package com.jaramos2409.travelbuy.datamodels;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.File;
import java.util.Locale;

/**
 * Created by EVA Unit 02 on 11/20/2016.
 */
public class ShopItem implements Parcelable {
    private String mName;
    private double mPrice;
    private String mCategory;
    private String mDescription;
    private String mItemId;
    private String mShopId;
    private String mItemPhotoPath;

    public ShopItem(String name, double price, String category, String description, String itemId, String shopId, String itemPhotoData) {
        this.mName = name;
        this.mPrice = price;
        this.mCategory = category;
        this.mDescription = description;
        this.mItemId = itemId;
        this.mShopId = shopId;
        this.mItemPhotoPath = itemPhotoData;
    }

    public ShopItem() {
        this.mName = "";
        this.mPrice = 0.0;
        this.mCategory = "";
        this.mDescription = "";
        this.mItemId = "";
        this.mShopId = "";
        this.mItemPhotoPath = "";
    }

    public ShopItem(Parcel in) {
        mName = in.readString();
        mPrice = in.readDouble();
        mShopId = in.readString();
        mCategory = in.readString();
        mDescription = in.readString();
        mItemId = in.readString();
        mItemPhotoPath = in.readString();
    }

    @Override
    public String toString() {
        return "Price: " + String.format(Locale.US, "%.2f", mPrice) + "\n" +
                "Category: " + mCategory + "\n" +
                "Description: " + mDescription + "\n";
    }

    @Override
    public int describeContents() {
        return hashCode();
    }

    /**
     * Actual object serialization happens here, Write object content
     * to parcel, reading should be done according to this write order
     * param dest - parcel
     * param flags - Additional flags about how the object should be written
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mName);
        dest.writeDouble(mPrice);
        dest.writeString(mShopId);
        dest.writeString(mCategory);
        dest.writeString(mDescription);
        dest.writeString(mItemId);
        dest.writeString(mItemPhotoPath);
    }

    /**
     * This field is needed for Android to be able to
     * create new objects, individually or as arrays
     *
     * If you don’t do that, Android framework will raises an exception
     * Parcelable protocol requires a Parcelable.Creator object
     * called CREATOR
     */
    public static final Parcelable.Creator<ShopItem> CREATOR = new Parcelable.Creator<ShopItem>() {

        public ShopItem createFromParcel(Parcel in) {
            return new ShopItem(in);
        }

        public ShopItem[] newArray(int size) {
            return new ShopItem[size];
        }
    };

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public double getPrice() {
        return mPrice;
    }

    public void setPrice(double price) {
        this.mPrice = price;
    }

    public String getCategory() {
        return mCategory;
    }

    public void setCategory(String category) {
        this.mCategory = category;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        this.mDescription = description;
    }

    public String getItemId() {
        return mItemId;
    }

    public void setItemId(String itemId) {
        this.mItemId = itemId;
    }

    public String getShopId() {
        return mShopId;
    }

    public void setShopId(String shopId) {
        this.mShopId = shopId;
    }

    public String getItemPhotoPath() {
        return mItemPhotoPath;
    }

    public void setItemPhotoPath(String itemPhotoPath) {
        mItemPhotoPath = itemPhotoPath;
    }
}
