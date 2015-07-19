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
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.yacorso.nowaste.R;
import com.yacorso.nowaste.models.CustomList;
import com.yacorso.nowaste.models.Food;
import com.yacorso.nowaste.models.User;
import com.yacorso.nowaste.providers.CustomListProvider;
import com.yacorso.nowaste.providers.FoodProvider;

import java.util.LinkedHashMap;
import java.util.List;

import butterknife.Bind;

public class AddFoodToCustomListDialog extends BaseFragment {

    @Bind(R.id.dialog_edit_text) EditText nameField;
    @Bind(R.id.dialog_text_input_layout) TextInputLayout tIL;

    public static AddFoodToCustomListDialog newInstance(Food food) {
        AddFoodToCustomListDialog addFoodToCustomListDialog = new AddFoodToCustomListDialog();

        Bundle args = new Bundle();
        args.putParcelable("food", food);
        addFoodToCustomListDialog.setArguments(args);
        return addFoodToCustomListDialog;
    }

    public AddFoodToCustomListDialog() {
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
        Bundle arguments = getArguments();
        final Food food = arguments.getParcelable("food");

        final User user = food.getFridge().getUser();
        final List<CustomList> customLists = user.getCustomLists();
        String[] values = new String[customLists.size()];
        final LinkedHashMap<Integer, Long> ids = new LinkedHashMap<>();

        for (int i = 0; i < customLists.size(); i++) {
            values[i] = customLists.get(i).getName();
            ids.put(i, customLists.get(i).getId());
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setItems(values, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                CustomListProvider customListProvider = new CustomListProvider();
                CustomList customList = customListProvider.get(ids.get(which));

                if (customList != null) {
                    Food newFood = new Food(food.getName());
                    newFood.setFoodList(customList);

                    FoodProvider foodProvider = new FoodProvider();
                    foodProvider.create(newFood);
                }
            }
        });

        return builder.create();
    }

    @Override
    protected int getLayout() {
        return R.layout.dialog_add_foodlist;
    }

    @Override
    public int getTitle() {
        return R.string.title_add_food;
    }
}
