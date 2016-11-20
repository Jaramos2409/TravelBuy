package com.jaramos2409.travelbuy;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.UUID;

/**
 * Created by EVA Unit 02 on 11/4/2016.
 */
public class NearbyVendorFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.nearby_vendor_fragment, container, false);

        return view;
    }
}
