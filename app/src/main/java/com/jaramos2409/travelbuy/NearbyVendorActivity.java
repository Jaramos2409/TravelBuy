package com.jaramos2409.travelbuy;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.UUID;

/**
 * Created by EVA Unit 02 on 11/17/2016.
 */
public class NearbyVendorActivity extends AppCompatActivity {

    public static Intent newIntent(Context packageContext) {
        Intent intent = new Intent(packageContext, NearbyVendorActivity.class);
        //intent.putExtra(EXTRA_CRIME_ID, crimeId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nearby_vendor_fragment);
    }
}
