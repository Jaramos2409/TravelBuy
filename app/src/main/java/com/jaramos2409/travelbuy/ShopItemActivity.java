package com.jaramos2409.travelbuy;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.jaramos2409.travelbuy.database.DBTask;
import com.jaramos2409.travelbuy.database.DBTypes;
import com.jaramos2409.travelbuy.datamodels.Shop;
import com.jaramos2409.travelbuy.datamodels.ShopItem;

import org.apache.commons.lang3.math.NumberUtils;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ShopItemActivity extends AppCompatActivity {
    private static final String EXTRA_IS_EDIT_ITEM = "com.jaramos2409.travelbuy.is_edit_item";
    private static final String EXTRA_ITEM_TO_EDIT = "com.jaramos2409.travelbuy.edit_item";

    @BindView(R.id.item_name_text) EditText mItemNameText;
    @BindView(R.id.image_view) ImageView mItemImage;
    @BindView(R.id.description_text) EditText mDescriptionText;
    @BindView(R.id.price_text) EditText mPriceText;
    @BindView(R.id.item_category) Spinner mCategorySpinner;
    @BindView(R.id.add_item_button) Button mAddEditItemBtn;

    private Context context = this;

    private boolean isEditItem;
    private ShopItem itemToEdit;

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
        }
    }

    @OnClick(R.id.add_item_button) public void onClick(View v) {
        String name = mItemNameText.getText().toString();
        String picture = "Picture";
        String description = mDescriptionText.getText().toString();
        String priceText = mPriceText.getText().toString();
        String category = mCategorySpinner.getSelectedItem().toString();

        if (!name.isEmpty() && !picture.isEmpty()
                && !description.isEmpty() && !priceText.isEmpty() && !category.isEmpty()) {

            if (NumberUtils.isCreatable(priceText)) {
                double price = Double.parseDouble(priceText);

                if (isEditItem)
                {
                    new DBTask(context, new ShopItem(name, price, category,
                            description, picture, itemToEdit.getItemId(), Shop.getCurrentShopInfo().getShopId()))
                            .execute(DBTypes.INSERT_ITEM);
                } else {
                    new DBTask(context, new ShopItem(name, price, category,
                            description, picture, "", Shop.getCurrentShopInfo().getShopId()))
                            .execute(DBTypes.INSERT_ITEM);
                }

                Intent intent = ShopItemsActivity.newIntent(context);
                setResult(RESULT_OK, intent);
                finish();
                onBackPressed();
            } else {
                Toast.makeText(context, "Error: Invalid Price.", Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(context, "Error: Field left empty.", Toast.LENGTH_SHORT).show();
        }
    }
}
