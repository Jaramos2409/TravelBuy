package com.jaramos2409.travelbuy;

import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.jaramos2409.travelbuy.database.DBQueryHandler;
import com.jaramos2409.travelbuy.datamodels.ShopItem;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by EVA Unit 02 on 11/19/2016.
 */
public class SearchResultsActivity extends AppCompatActivity {

    private final static String EXTRA_CATEGORY = "com.jaramos2409.travelbuy.category";

    @BindView(R.id.search_items_refresh) SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.search_results_list) ListView mSearchItemListView;

    private ArrayList<ShopItem> mShopItemsSearchResultsList;
    private ShopItemsListAdapter mShopItemsListAdapter;

    private Context context = this;

    public static Intent newIntent(Context packageContext, String query, String category) {
        Intent intent = new Intent(packageContext, SearchResultsActivity.class);
        intent.setAction(Intent.ACTION_SEARCH);
        intent.putExtra(SearchManager.QUERY, query);
        intent.putExtra(EXTRA_CATEGORY, category);
        return intent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_results_activity);
        ButterKnife.bind(this);

        if(getSupportActionBar() != null)
        {
            getSupportActionBar().setTitle("TravelBuy - Search");
        }

        mShopItemsSearchResultsList = new ArrayList<>();
        mShopItemsListAdapter = new ShopItemsListAdapter(this, mShopItemsSearchResultsList);

        mSwipeRefreshLayout.setOnRefreshListener(swipeRefreshHandler);

        registerForContextMenu(mSearchItemListView);

        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            String category = intent.getStringExtra(EXTRA_CATEGORY);
            doMySearch(query, category);
        }
    }

    private void doMySearch(String query, String category) {
        swipeRefreshHandler.setQuery(query);
        swipeRefreshHandler.setCategory(category);
        setRefreshProgrammatically();
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

    private void updateListData() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mShopItemsListAdapter.refreshEvents(mShopItemsSearchResultsList);
                mSearchItemListView.setAdapter(mShopItemsListAdapter);
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private RefreshCustomHandler swipeRefreshHandler = new RefreshCustomHandler();

    private class RefreshCustomHandler implements SwipeRefreshLayout.OnRefreshListener {
        private String mQuery;
        private String mCategory;

        public RefreshCustomHandler() {
            mQuery = "";
            mCategory = "";
        }

        public RefreshCustomHandler(String query, String category) {
            mQuery = query;
            mCategory = category;
        }

        @Override
        public void onRefresh() {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    mShopItemsSearchResultsList = DBQueryHandler.searchShopItems(context, mQuery, mCategory);
                    updateListData();
                }
            }).start();
        }

        public String getQuery() {
            return mQuery;
        }

        public void setQuery(String query) {
            mQuery = query;
        }

        public String getCategory() {
            return mCategory;
        }

        public void setCategory(String category) {
            mCategory = category;
        }
    }
}
