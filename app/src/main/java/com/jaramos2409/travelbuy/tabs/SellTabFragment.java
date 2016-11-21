package com.jaramos2409.travelbuy.tabs;

import android.content.Intent;
import android.os.Bundle;
        import android.support.v4.app.Fragment;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
import android.widget.Button;

import com.jaramos2409.travelbuy.R;
import com.jaramos2409.travelbuy.ShopItemsActivity;
import com.jaramos2409.travelbuy.login.SignInActivity;
import com.jaramos2409.travelbuy.datamodels.Shop;

public class SellTabFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view;
        if(Shop.getCurrentShopInfo().isSelling()) {
            view = inflater.inflate(R.layout.sell_tab_fragment_existing_shop, container, false);

            Button manageItemsBtn = (Button) view.findViewById(R.id.manage_items_btn);
            manageItemsBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = ShopItemsActivity.newIntent(getActivity());
                    startActivity(intent);
                }
            });

            Button closeOpenBtn = (Button) view.findViewById(R.id.close_open_shop_btn);

            if(Shop.getCurrentShopInfo().isOpen())  closeOpenBtn.setText(R.string.open_shop);

            closeOpenBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        } else {
            view = inflater.inflate(R.layout.sell_tab_fragment_new_shop, container, false);

            Button createLoginBtn;
            createLoginBtn = (Button) view.findViewById(R.id.create_shop_btn);
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
}