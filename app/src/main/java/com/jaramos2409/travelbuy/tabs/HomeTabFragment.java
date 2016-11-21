package com.jaramos2409.travelbuy.tabs;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jaramos2409.travelbuy.R;
import com.jaramos2409.travelbuy.datamodels.Shop;

public class HomeTabFragment extends Fragment {

    private TextView userInfo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_tab_fragment, container, false);

        userInfo = (TextView) view.findViewById(R.id.user_info);
        userInfo.setText(Shop.getCurrentShopInfo().toString());
        //userInfo.setText(AWSMobileClient.defaultMobileClient().getIdentityManager().getCachedUserID());

        return view;
    }
}