package com.jaramos2409.travelbuy;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser;
import com.jaramos2409.travelbuy.database.DBTask;
import com.jaramos2409.travelbuy.database.DBTypes;

import java.util.UUID;

public class TravelBuyActivity extends SingleFragmentActivity {

    private static final String EXTRA_IS_SELLING = "com.jaramos2409.travelbuy.android.is_selling";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getIntent().getBooleanExtra(EXTRA_IS_SELLING, false)) {
            new DBTask(this).execute(DBTypes.LOAD_SHOP);
        }

    }

    public static Intent newIntent(Context packageContext, boolean isSelling) {
        Intent intent = new Intent(packageContext, TravelBuyActivity.class);
        intent.putExtra(EXTRA_IS_SELLING, isSelling);
        return intent;
    }

    @Override
    protected Fragment createFragment() {
        return new MainMenuFragment();
    }
}
