package com.jaramos2409.travelbuy;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

public class TravelBuyActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new MainMenuFragment();
    }
}
