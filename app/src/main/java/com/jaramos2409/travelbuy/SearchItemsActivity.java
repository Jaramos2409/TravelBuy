package com.jaramos2409.travelbuy;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Spinner;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SearchItemsActivity extends AppCompatActivity {

    @BindView(R.id.item_search_view) SearchView mSearchView;
    @BindView(R.id.item_category_spinner_search) Spinner mCategorySpinner;
    @BindView(R.id.search_by_category) Button mSearchByCategoryBtn;

    private Context context = this;

    public static Intent newIntent(Context packageContext) {
        Intent intent = new Intent(packageContext, SearchItemsActivity.class);
        //intent.putExtra(EXTRA_CRIME_ID, crimeId);
        return intent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_items_activity);
        ButterKnife.bind(this);

        if(getSupportActionBar() != null)
        {
            getSupportActionBar().setTitle("TravelBuy - Search");
        }

        mSearchView.setQueryHint("Search View");

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                String category = mCategorySpinner.getSelectedItem().toString();
                Intent intent = SearchResultsActivity.newIntent(context, query, category);
                startActivity(intent);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //Toast.makeText(getBaseContext(), newText, Toast.LENGTH_LONG).show();
                return false;
            }
        });
    }

    @OnClick (R.id.search_by_category)
    public void onClick(View v) {
        String category = mCategorySpinner.getSelectedItem().toString();
        Intent intent = SearchResultsActivity.newIntent(context, "", category);
        startActivity(intent);
    }
}
