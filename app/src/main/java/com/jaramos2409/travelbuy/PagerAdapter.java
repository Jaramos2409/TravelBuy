package com.jaramos2409.travelbuy;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.jaramos2409.travelbuy.tabs.BuyTabFragment;
import com.jaramos2409.travelbuy.tabs.FriendTabFragment;
import com.jaramos2409.travelbuy.tabs.HomeTabFragment;
import com.jaramos2409.travelbuy.tabs.SellTabFragment;

public class PagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public PagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                HomeTabFragment homeTab = new HomeTabFragment();
                return homeTab;
            case 1:
                BuyTabFragment buyTab = new BuyTabFragment();
                return buyTab;
            case 2:
                SellTabFragment sellTab = new SellTabFragment();
                return sellTab;
            case 3:
                FriendTabFragment friendTab = new FriendTabFragment();
                return friendTab;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}