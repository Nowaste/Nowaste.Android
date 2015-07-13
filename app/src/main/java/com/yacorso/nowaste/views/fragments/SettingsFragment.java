/*
 * Copyright (c) 2015.
 *
 * "THE BEER-WARE LICENSE" (Revision 42):
 * Quentin Bontemps <q.bontemps@gmail>  , Florian Garnier <reventlov@tuta.io>
 * and Marjorie Déboté <marjorie.debote@free.com> wrote this file.
 * As long as you retain this notice you can do whatever you want with this stuff.
 * If we meet some day, and you think this stuff is worth it, you can buy me a beer in return.
 *
 * NoWaste team
 */

package com.yacorso.nowaste.views.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.yacorso.nowaste.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by quentin on 05/07/15.
 */
public class SettingsFragment extends BaseFragment {

    private Spinner mySpinner;

    public SettingsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_settings, container, false);
        mRootView = super.onCreateView(inflater, container, savedInstanceState);

        setSpinnerContent(layout);

        return mRootView;
    }

    private void setSpinnerContent(View view){
        mySpinner = (Spinner) view.findViewById(R.id.spinner_notifications);

        List<String> list = new ArrayList<String>();
        list.add("1 jour");
        list.add("2 jour");
        list.add("3 jour");
        list.add("5 jour");
        list.add("1 semaine");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
                (getActivity(), android.R.layout.simple_spinner_item,list);
        dataAdapter.setDropDownViewResource
                (android.R.layout.simple_spinner_item);
        mySpinner.setAdapter(dataAdapter);
    }

    public static SettingsFragment newInstance(){
        return new SettingsFragment();
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_settings;
    }


}
