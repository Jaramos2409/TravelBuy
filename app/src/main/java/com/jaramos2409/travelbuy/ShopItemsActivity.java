package com.jaramos2409.travelbuy;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.jaramos2409.travelbuy.database.DBQueryHandler;
import com.jaramos2409.travelbuy.datamodels.ShopItem;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemClick;

/**
 * Created by EVA Unit 02 on 11/20/2016.
 */
public class ShopItemsActivity extends AppCompatActivity {
    private static final int RESULT_ADD_EDIT_ITEM = 450;

    @BindView(R.id.shop_items_listview) ListView mSearchItemListView;
    @BindView(R.id.shop_items_refresh) SwipeRefreshLayout mSwipeRefreshLayout;

    private ArrayList<ShopItem> mShopItemsList;
    private ShopItemsListAdapter mShopItemsListAdapter;

    private Context context = this;

    public static Intent newIntent(Context packageContext) {
        Intent intent = new Intent(packageContext, ShopItemsActivity.class);
        //intent.putExtra(EXTRA_ADDED_NEW_ITEM, addedNewItem);
        return intent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shop_items_activity);
        ButterKnife.bind(this);

        mShopItemsList = new ArrayList<>();
        mShopItemsListAdapter = new ShopItemsListAdapter(this, mShopItemsList);

        mSwipeRefreshLayout.setOnRefreshListener(swipeRefreshHandler);
        setRefreshProgrammatically();

        Toast.makeText(context, "Press on the Item you want to edit.", Toast.LENGTH_LONG).show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.shop_items_list_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.menu_add_item:
                Intent intent = ShopItemActivity.newIntent(this);
                startActivityForResult(intent, RESULT_ADD_EDIT_ITEM);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onResume () {
        super.onResume();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_ADD_EDIT_ITEM) {
            if(resultCode == RESULT_OK){
                setRefreshProgrammatically();
            }
        }
    }

    private SwipeRefreshLayout.OnRefreshListener swipeRefreshHandler = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    mShopItemsList = DBQueryHandler.loadShopItems();
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
                mSearchItemListView.setAdapter(mShopItemsListAdapter);
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

    @OnItemClick(R.id.shop_items_listview)
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ShopItem shopItem = mShopItemsList.get(position);

        Intent intent = ShopItemActivity.newIntent(this, true, shopItem);
        startActivityForResult(intent, RESULT_ADD_EDIT_ITEM);
    }
}

