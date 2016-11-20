package com.jaramos2409.travelbuy.tabs;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.jaramos2409.travelbuy.NearbyVendorActivity;
import com.jaramos2409.travelbuy.R;
import com.jaramos2409.travelbuy.SearchItemsActivity;

public class BuyTabFragment extends Fragment {

    private Button mNearbyVendorsBtn;
    private Button mSearchItemsBtn;
    private Button mMessagesVendorsBtn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.buy_tab_fragment, container, false);

        mNearbyVendorsBtn = (Button) view.findViewById(R.id.nearby_vendors_btn);
        mNearbyVendorsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = NearbyVendorActivity.newIntent(getActivity());
                startActivity(intent);
            }
        });

        mSearchItemsBtn = (Button) view.findViewById(R.id.search_items_btn);
        mSearchItemsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = SearchItemsActivity.newIntent(getActivity());
                startActivity(intent);
            }
        });

        mMessagesVendorsBtn = (Button) view.findViewById(R.id.message_vendors_btn);
        mMessagesVendorsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return view;
    }


}