package com.jaramos2409.travelbuy;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.jaramos2409.travelbuy.database.DBQueryHandler;
import com.jaramos2409.travelbuy.database.DBTask;
import com.jaramos2409.travelbuy.database.DBTypes;
import com.jaramos2409.travelbuy.datamodels.Shop;
import com.jaramos2409.travelbuy.datamodels.ShopItem;

import org.apache.commons.lang3.math.NumberUtils;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.jaramos2409.travelbuy.TravelBuyActivity.READ_EXST;

public class ShopItemActivity extends AppCompatActivity {

    private static final String LOG_TAG = ShopItemActivity.class.getSimpleName();

    private static final String EXTRA_IS_EDIT_ITEM = "com.jaramos2409.travelbuy.is_edit_item";
    private static final String EXTRA_ITEM_TO_EDIT = "com.jaramos2409.travelbuy.edit_item";

    private static final int RESULT_LOAD_IMG = 550;

    @BindView(R.id.item_name_text) EditText mItemNameText;
    @BindView(R.id.item_photo) ImageView mItemPhoto;
    @BindView(R.id.description_text) EditText mDescriptionText;
    @BindView(R.id.price_text) EditText mPriceText;
    @BindView(R.id.item_category) Spinner mCategorySpinner;
    @BindView(R.id.add_item_button) Button mAddEditItemBtn;

    private Context context = this;
    private ProgressDialog mProgressDialog;

    private boolean isEditItem;
    private ShopItem itemToEdit;

    private String photoPath;
    private AddUpdateItemTask task;

    public static Intent newIntent(Context packageContext) {
        Intent intent = new Intent(packageContext, ShopItemActivity.class);
        return intent;
    }

    public static Intent newIntent(Context packageContext, boolean isEditItem, ShopItem shopItem) {
        Intent intent = new Intent(packageContext, ShopItemActivity.class);
        intent.putExtra(EXTRA_IS_EDIT_ITEM, isEditItem);
        intent.putExtra(EXTRA_ITEM_TO_EDIT, shopItem);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_item);
        ButterKnife.bind(this);

        photoPath = "";
        task = null;

        isEditItem = getIntent().getBooleanExtra(EXTRA_IS_EDIT_ITEM, false);

        if (isEditItem) {
            itemToEdit = getIntent().getParcelableExtra(EXTRA_ITEM_TO_EDIT);

            mItemNameText.setText(itemToEdit.getName(), TextView.BufferType.EDITABLE);
            mDescriptionText.setText(itemToEdit.getDescription(), TextView.BufferType.EDITABLE);
            mPriceText.setText(String.format(Locale.US, "%.2f", itemToEdit.getPrice()), TextView.BufferType.EDITABLE);

            String compareValue = itemToEdit.getCategory();
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                    R.array.category_array_user, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mCategorySpinner.setAdapter(adapter);
            if (!compareValue.equals("")) {
                int spinnerPosition = adapter.getPosition(compareValue);
                mCategorySpinner.setSelection(spinnerPosition);
            }

            mAddEditItemBtn.setText(R.string.edit_item);

            Bitmap image = BitmapFactory.decodeFile(itemToEdit.getItemPhotoPath());
            int height = 400;
            int width = 711;
            Bitmap thumbnail =  ThumbnailUtils.extractThumbnail(image, width, height);
            ImageView imgView = (ImageView) findViewById(R.id.item_photo);
            imgView.setImageBitmap(thumbnail);

            mItemPhoto.setImageBitmap(thumbnail);
        }
    }

    @OnClick(R.id.add_item_button) public void onClick(View v) {
        String name = mItemNameText.getText().toString();
        String description = mDescriptionText.getText().toString();
        String priceText = mPriceText.getText().toString();
        String category = mCategorySpinner.getSelectedItem().toString();

        if (!name.isEmpty() && !description.isEmpty() && !priceText.isEmpty() && !category.isEmpty()) {

            if (NumberUtils.isCreatable(priceText) || !photoPath.isEmpty()) {
                double price = Double.parseDouble(priceText);

                if (isEditItem)
                {
                    new AddUpdateItemTask(new ShopItem(name, price, category,
                            description, itemToEdit.getItemId(), Shop.getCurrentShopInfo().getShopId(), photoPath))
                            .execute(DBTypes.INSERT_ITEM);
                } else {
                    task = new AddUpdateItemTask(new ShopItem(name, price, category,
                            description, "", Shop.getCurrentShopInfo().getShopId(), photoPath));
                    task.execute(DBTypes.INSERT_ITEM);

                    Log.d(LOG_TAG, "Task was cancelled: " + task.isCancelled());
                }

                Intent intent = ShopItemsActivity.newIntent(context);
                setResult(RESULT_OK, intent);
                finish();
                onBackPressed();
            } else {
                Toast.makeText(context, "Error: Invalid Price or Photo", Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(context, "Error: Field left empty.", Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.item_photo) public void OnClick(View w) {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK
                    && null != data) {
                Uri selectedImage = data.getData();

                String[] filePathColumn = { MediaStore.Images.Media.DATA };

                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String imgDecodableString = cursor.getString(columnIndex);
                photoPath = imgDecodableString;
                cursor.close();

                Bitmap image = BitmapFactory.decodeFile(imgDecodableString);

                int height = 400;
                int width = 711;

                Bitmap thumbnail =  ThumbnailUtils.extractThumbnail(image, width, height);

                ImageView imgView = (ImageView) findViewById(R.id.item_photo);
                imgView.setImageBitmap(thumbnail);

            } else {
                Toast.makeText(this, "You haven't picked Image",
                        Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG)
                    .show();
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        dismissProgressDialog();
        super.onDestroy();
    }

    private void dismissProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    class AddUpdateItemTask extends
            AsyncTask<String, Void, Void> {

        private ShopItem mShopItem;
        private ArrayList<ShopItem> mShopItemsList;

        public AddUpdateItemTask(Context context) {
            mShopItem = new ShopItem();
            mShopItemsList = new ArrayList<>();
        }

        public AddUpdateItemTask() {
            super();
            mShopItem = new ShopItem();
            mShopItemsList = new ArrayList<>();
        }

        public AddUpdateItemTask(ShopItem shopItem) {
            super();
            mShopItem = shopItem;
            mShopItemsList = new ArrayList<>();
        }

        @Override
        protected void onPreExecute() {
            mProgressDialog = new ProgressDialog(context);
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
            DBQueryHandler.insertShopItem(mShopItem, context);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            if (ShopItemActivity.this.isDestroyed()) { // or call isFinishing() if min sdk version < 17
                return;
            }

            dismissProgressDialog();

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

}
