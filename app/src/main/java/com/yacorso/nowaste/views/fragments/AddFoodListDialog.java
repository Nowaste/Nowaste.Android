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
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.yacorso.nowaste.R;
import com.yacorso.nowaste.models.CustomList;
import com.yacorso.nowaste.models.FoodList;
import com.yacorso.nowaste.models.Fridge;
import com.yacorso.nowaste.providers.CustomListProvider;
import com.yacorso.nowaste.providers.FridgeProvider;

import butterknife.Bind;
import butterknife.ButterKnife;

public class AddFoodListDialog extends BaseFragment {

    @Bind(R.id.dialog_edit_text) EditText nameField;
    @Bind(R.id.dialog_text_input_layout) TextInputLayout tIL;

    public static AddFoodListDialog newInstance(FoodList foodList) {
        AddFoodListDialog addFoodListDialog = new AddFoodListDialog();

        Bundle args = new Bundle();
        args.putParcelable("foodList", foodList);
        addFoodListDialog.setArguments(args);
        return addFoodListDialog;
    }

    public AddFoodListDialog() {
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
        final FoodList foodList = arguments.getParcelable("foodList");

        mRootView = getActivity().getLayoutInflater().inflate(getLayout(), null);
        ButterKnife.bind(this, mRootView);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(mRootView);

        if (foodList instanceof CustomList) {
            tIL.setHint(getString(R.string.name_customlist_hint));
        }
        nameField.requestFocus();
        nameField.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                return false;
            }
        });

        builder.setNegativeButton(android.R.string.cancel, null);
        builder.setPositiveButton(android.R.string.ok, null);

        final AlertDialog dialog = builder.create();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dI) {
                Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positiveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String name = nameField.getText().toString();
                        if (isInputNotEmpty(name)) {
                            foodList.setName(name);
                            if (foodList instanceof Fridge) {
                                new FridgeProvider().create((Fridge) foodList);
                            } else {
                                new CustomListProvider().create((CustomList) foodList);
                            }
                            dialog.dismiss();
                        }
                    }
                });
                Button negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                negativeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
            }
        });

        return dialog;
    }

    @Override
    protected int getLayout() {
        return R.layout.dialog_add_foodlist;
    }

    @Override
    public int getTitle() {
        return R.string.title_add_food;
    }

    private boolean isInputNotEmpty(String name) {
        if (name.isEmpty()) {
            tIL.setError(getResources().getString(R.string.name_mandatory));
            return false;
        }
        tIL.setError("");
        return true;
    }
}
