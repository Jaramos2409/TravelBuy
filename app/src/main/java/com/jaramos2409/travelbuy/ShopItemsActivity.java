package com.jaramos2409.travelbuy;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.jaramos2409.travelbuy.datamodels.ShopItem;

import java.util.ArrayList;

/**
 * Created by EVA Unit 02 on 11/20/2016.
 */
public class ShopItemsActivity extends AppCompatActivity {

    private SearchView searchView;
    private MenuItem searchMenuItem;
    private ListView searchItemListView;
    private ArrayList<ShopItem> shopItemsList;

    public static Intent newIntent(Context packageContext) {
        Intent intent = new Intent(packageContext, SearchItemsActivity.class);
        //intent.putExtra(EXTRA_CRIME_ID, crimeId);
        return intent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shop_items_activity);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);

        return true;
    }

}
