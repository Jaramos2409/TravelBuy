package com.jaramos2409.travelbuy;

import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.jaramos2409.travelbuy.database.DBQueryHandler;
import com.jaramos2409.travelbuy.datamodels.ShopItem;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemClick;

public class VendorShopItemsActivity extends AppCompatActivity {
    private static final String EXTRA_SHOP_ID = "com.jaramos2409.travelbuy.extra_shop_id";

    @BindView(R.id.vendor_shop_items_listview) ListView mVendorShopListView;
    @BindView(R.id.vendor_shop_refresh) SwipeRefreshLayout mSwipeRefreshLayout;

    private ArrayList<ShopItem> mShopItemsList;
    private ShopItemsListAdapter mShopItemsListAdapter;

    private Context context = this;

    private String shopId;

    public static Intent newIntent(Context packageContext, String shopId) {
        Intent intent = new Intent(packageContext, VendorShopItemsActivity.class);
        intent.putExtra(EXTRA_SHOP_ID, shopId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_shop_items);
        ButterKnife.bind(this);

        shopId = getIntent().getStringExtra(EXTRA_SHOP_ID);

        mShopItemsList = new ArrayList<>();
        mShopItemsListAdapter = new ShopItemsListAdapter(this, mShopItemsList);

        mSwipeRefreshLayout.setOnRefreshListener(swipeRefreshHandler);
        setRefreshProgrammatically();

    }

    private SwipeRefreshLayout.OnRefreshListener swipeRefreshHandler = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    mShopItemsList = DBQueryHandler.loadShopItems(context, shopId);
                    updateListData();
                }
            }).start();
        }
    };

    private void updateListData() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mShopItemsListAdapter.refreshEvents(mShopItemsList);
                mVendorShopListView.setAdapter(mShopItemsListAdapter);
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void setRefreshProgrammatically() {
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(true);
            }
        });

        swipeRefreshHandler.onRefresh();
    }

    @OnItemClick(R.id.vendor_shop_items_listview)
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ShopItem shopItem = mShopItemsList.get(position);

        Intent intent = ShopItemVendorActivity.newIntent(this, shopItem);
        startActivity(intent);
    }
}
