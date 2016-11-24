package com.jaramos2409.travelbuy;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.jaramos2409.travelbuy.datamodels.Shop;
import com.jaramos2409.travelbuy.datamodels.ShopItem;

import java.util.ArrayList;

/**
 * Created by EVA Unit 02 on 11/20/2016.
 */
public class ShopItemsListAdapter extends ArrayAdapter {
    ArrayList<ShopItem> mShopItemArrayList;
    Context context;

    public ShopItemsListAdapter(Activity context, ArrayList<ShopItem> shopItemsList) {
        super(context, R.layout.shop_item_listview, shopItemsList);
        mShopItemArrayList = shopItemsList;
        this.context = context;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater layoutInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View viewRow = layoutInflater.inflate(R.layout.shop_item_listview, null,
                true);

        TextView itemNameTextView = (TextView) viewRow.findViewById(R.id.list_title_text);
        TextView itemInfoTextView = (TextView) viewRow.findViewById(R.id.list_subtitle_text);

        ShopItem shopItem = mShopItemArrayList.get(i);

        itemNameTextView.setText(shopItem.getName());
        itemInfoTextView.setText(shopItem.toString());

        return viewRow;
    }

    public void refreshEvents(ArrayList<ShopItem> events) {
        this.mShopItemArrayList.clear();
        this.mShopItemArrayList.addAll(events);
        notifyDataSetChanged();
    }
}
