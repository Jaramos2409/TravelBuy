package com.jaramos2409.travelbuy.tabs;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jaramos2409.travelbuy.R;

/**
 * Created by EVA Unit 02 on 11/4/2016.
 */
public class FriendTabFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.friend_tab_fragment, container, false);
    }
}