package com.jaramos2409.travelbuy;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jaramos2409.travelbuy.database.DBQueryHandler;
import com.jaramos2409.travelbuy.database.DBTask;
import com.jaramos2409.travelbuy.database.DBTypes;
import com.jaramos2409.travelbuy.datamodels.Shop;
import com.jaramos2409.travelbuy.datamodels.ShopItem;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by EVA Unit 02 on 11/27/2016.
 */

public class ShopItemVendorActivity extends AppCompatActivity {
    private static final String LOG_TAG = ShopItemEditAddActivity.class.getSimpleName();
    private static final String EXTRA_SHOP_ITEM = "com.jaramos2409.travelbuy.extra_shop_item";

    @BindView(R.id.item_photo_vendor) ImageView mItemPhotoImageView;
    @BindView(R.id.item_name_text_vendor) TextView mItemNameTextView;
    @BindView(R.id.price_text_vendor) TextView mPriceTextView;
    @BindView(R.id.description_text_vendor) TextView mDescriptionTextView;
    @BindView(R.id.email_vendor_btn) Button mEmailBtn;
    @BindView(R.id.vendor_shop_btn) Button mGoToVendorBtn;
    @BindView(R.id.report_shop_item_btn) Button mReportItemBtn;

    private Context context = this;

    private ShopItem mShopItem;
    private String mEmail;
    private static final String mDevEmail = "jaramos2409@gmail.com";

    public static Intent newIntent(Context packageContext, ShopItem shopItem) {
        Intent intent = new Intent(packageContext, ShopItemVendorActivity.class);
        intent.putExtra(EXTRA_SHOP_ITEM, shopItem);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_item_vendor);
        ButterKnife.bind(this);

        mShopItem = getIntent().getParcelableExtra(EXTRA_SHOP_ITEM);
        mItemNameTextView.setText(mShopItem.getName());
        mPriceTextView.setText(String.format(Locale.US, "%.2f", mShopItem.getPrice()));
        mDescriptionTextView.setText(mShopItem.getDescription());

        Bitmap image = BitmapFactory.decodeFile(mShopItem.getItemPhotoPath());
        int height = 400;
        int width = 711;
        Bitmap thumbnail =  ThumbnailUtils.extractThumbnail(image, width, height);
        ImageView imgView = (ImageView) findViewById(R.id.item_photo_vendor);
        imgView.setImageBitmap(thumbnail);

        mItemPhotoImageView.setImageBitmap(thumbnail);

        mEmailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Shop.getCurrentShopInfo().isSelling()) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            mEmail = DBQueryHandler.getShopEmail(mShopItem.getShopId());
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Intent intent = new Intent(Intent.ACTION_SEND);
                                    intent.setData(Uri.parse("mailto:"));
                                    intent.setType("text/plain");
                                    intent.putExtra(Intent.EXTRA_EMAIL, new String[]{mEmail});
                                    intent.putExtra(Intent.EXTRA_SUBJECT, "Interested in Item: " + mShopItem.getName());
                                    startActivity(Intent.createChooser(intent, "Choose an Email client :"));
                                }
                            });
                        }
                    }).start();
                } else {
                    Toast.makeText(context, "Please login in order to email vendor." , Toast.LENGTH_SHORT).show();
                }
            }
        });

        mGoToVendorBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = VendorShopItemsActivity.newIntent(context, mShopItem.getShopId());
                startActivity(intent);
            }
        });

        mReportItemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setData(Uri.parse("mailto:"));
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{mDevEmail});
                intent.putExtra(Intent.EXTRA_SUBJECT, "Report on Item: " + mShopItem.getName() + " ID: " + mShopItem.getItemId());
                startActivity(Intent.createChooser(intent, "Choose an Email client :"));
            }
        });
    }


}
