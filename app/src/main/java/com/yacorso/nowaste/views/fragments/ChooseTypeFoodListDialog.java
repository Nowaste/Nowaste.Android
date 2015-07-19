/*
 *
 *  * Copyright (c) 2015.
 *  *
 *  * "THE BEER-WARE LICENSE" (Revision 42):
 *  * Quentin Bontemps <q.bontemps@gmail>  , Florian Garnier <reventlov@tuta.io>
 *  * and Marjorie Déboté <marjorie.debote@free.com> wrote this file.
 *  * As long as you retain this notice you can do whatever you want with this stuff.
 *  * If we meet some day, and you think this stuff is worth it, you can buy me a beer in return.
 *  *
 *  * NoWaste team
 *
 */

package com.yacorso.nowaste.views.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.yacorso.nowaste.R;
import com.yacorso.nowaste.events.CallAddFoodListEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.*;
import de.greenrobot.event.EventBus;
import de.hdodenhof.circleimageview.CircleImageView;

public class ChooseTypeFoodListDialog extends BaseFragment {

    public static ChooseTypeFoodListDialog newInstance() {
        return new ChooseTypeFoodListDialog();
    }

    public ChooseTypeFoodListDialog() {
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return null;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        Resources res = getResources();
        TypedArray icons = res.obtainTypedArray(R.array.foodlist_icon);
        TypedArray names = res.obtainTypedArray(R.array.foodlist_name);

        List<HashMap<String, Object>> aList = new ArrayList<>();
        for(int i=0; i<icons.length(); i++){
            HashMap<String, Object> hm = new HashMap<>();
            hm.put("icon", icons.getResourceId(i, 0));
            hm.put("text",  names.getString(i));
            aList.add(hm);
        }

        FoodListAdapter adapter = new FoodListAdapter(getActivity().getApplicationContext(), aList);

        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dismiss();
                EventBus.getDefault().post(new CallAddFoodListEvent(which));
            }
        });

        return builder.create();
    }

    @Override
    protected int getLayout() {
        return R.layout.dialog_choose_foodlist;
    }

    @Override
    public int getTitle() {
        return R.string.title_add_food;
    }

    public class FoodListAdapter extends ArrayAdapter<HashMap<String, Object>> {

        @Bind(R.id.dialog_text) TextView foodListTextView;
        @Bind(R.id.dialog_circle_image) CircleImageView circleImage;

        public FoodListAdapter(Context context, List<HashMap<String, Object>> foodLists) {
            super(context, 0, foodLists);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            HashMap<String, Object> foodList = getItem(position);

            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(getLayout(), parent, false);
            }
            ButterKnife.bind(this, convertView);

            foodListTextView.setText((String) foodList.get("text"));
            circleImage.setImageResource((int) foodList.get("icon"));

            return convertView;
        }
    }
}
