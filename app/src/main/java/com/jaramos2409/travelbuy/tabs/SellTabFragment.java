package com.jaramos2409.travelbuy.tabs;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsResult;
import com.jaramos2409.travelbuy.ManageItemsActivity;
import com.jaramos2409.travelbuy.R;
import com.jaramos2409.travelbuy.database.DBQueryHandler;
import com.jaramos2409.travelbuy.login.SignInActivity;
import com.jaramos2409.travelbuy.datamodels.Shop;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SellTabFragment extends Fragment {

    private final static String LOG_TAG = "SellTabFragment";

    static final Integer GPS_SETTINGS = 0x7;

    @Nullable @BindView(R.id.manage_items_btn) Button manageItemsBtn;
    @Nullable @BindView(R.id.set_shop_location_button) Button mSetShopLocationBtn;
    @Nullable @BindView(R.id.close_open_shop_btn) Button closeOpenBtn;
    @Nullable @BindView(R.id.create_shop_btn) Button createLoginBtn;

    private GoogleApiClient client;
    private PendingResult<LocationSettingsResult> result;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view;

        if(Shop.getCurrentShopInfo().isSelling()) {
            view = inflater.inflate(R.layout.sell_tab_fragment_existing_shop, container, false);
            ButterKnife.bind(this, view);

            client = new GoogleApiClient.Builder(getActivity())
                    .addApi(LocationServices.API)
                    .build();

            manageItemsBtn.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent intent = ManageItemsActivity.newIntent(getActivity());
                    startActivity(intent);
                }
            });

            mSetShopLocationBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /*int off = 0;
                    try {
                        off = Settings.Secure.getInt(getActivity().getContentResolver(), Settings.Secure.LOCATION_MODE);
                    } catch (Settings.SettingNotFoundException e) {
                        e.printStackTrace();
                    }
                    if(off==0){
                        Intent onGPS = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(onGPS);
                    }*/
                    findLocation();
                }
            });

            if(Shop.getCurrentShopInfo().isOpen())  closeOpenBtn.setText(R.string.open_shop);

            closeOpenBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        } else {
            view = inflater.inflate(R.layout.sell_tab_fragment_new_shop, container, false);
            ButterKnife.bind(this, view);

            createLoginBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = SignInActivity.newIntent(getActivity());
                    startActivity(intent);
                }
            });
        }

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if(Shop.getCurrentShopInfo().isSelling()) {
            client.connect();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if(Shop.getCurrentShopInfo().isSelling()) {
            client.disconnect();
        }
    }

    private void findLocation() {
        LocationRequest request = LocationRequest.create();
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        request.setNumUpdates(1);
        request.setInterval(0);
        LocationServices.FusedLocationApi
                .requestLocationUpdates(client, request, new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        final Location foundLocation = location;
                        Shop.getCurrentShopInfo().setLatitude(location.getLatitude());
                        Shop.getCurrentShopInfo().setLongitude(location.getLongitude());
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                DBQueryHandler.storeLocation(foundLocation);
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getActivity(), "Location Successfully Set", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }).start();
                    }
                });
    }

}