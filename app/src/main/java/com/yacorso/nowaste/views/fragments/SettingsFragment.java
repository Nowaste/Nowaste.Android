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
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;

import com.yacorso.nowaste.R;
import com.yacorso.nowaste.models.Configuration;
import com.yacorso.nowaste.models.User;
import com.yacorso.nowaste.providers.UserProvider;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by quentin on 05/07/15.
 */
public class SettingsFragment extends BaseFragment {

    private Spinner mySpinner;
    private EditText text_first_name; //EditText
    private EditText text_last_name;
    private EditText text_email;
    private EditText text_password;
    private Switch activate_notifications; //Switch

    LinearLayoutManager mLayoutManager;
    UserProvider mProvider;
    RecyclerView mRecyclerView;

    public SettingsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_settings, container, false);
        mRootView = super.onCreateView(inflater, container, savedInstanceState);

        Configuration maConfig = new Configuration();
        setMailContent(layout, maConfig);
        setFirstNameContent(layout, maConfig);
        setLastNameContent(layout, maConfig);
        setPasswordContent(layout, maConfig);
        //isNotificationContent(layout);

        setSpinnerContent(layout);
        return mRootView;
    }

    private void setMailContent(View view, Configuration config){
        text_email = (EditText) view.findViewById(R.id.text_email);
        String string_email = text_email.getText().toString();
        //config.setEmail(string_email);
        config.setKey("email");
        config.setValue("marjorie.debote@free.fr");
    }
    private void setFirstNameContent(View view, Configuration config){
        text_first_name = (EditText) view.findViewById(R.id.text_first_name);
        String string_first_name = text_first_name.getText().toString();
        //config.setFirstName(string_first_name);
        config.setKey("firstname");
        config.setValue("Debote");
    }
    private void setLastNameContent(View view, Configuration config){
        text_last_name = (EditText) view.findViewById(R.id.text_last_name);
        String string_last_name = text_last_name.getText().toString();
        //config.setLastName(string_last_name);
        config.setKey("lastname");
        config.setValue("Marjorie");
    }
    private void setPasswordContent(View view, Configuration config){
        text_password = (EditText) view.findViewById(R.id.text_password);
        String string_password = text_password.getText().toString();
        //config.setPassword(string_password);
        config.setKey("password");
        config.setValue("Password");
        //verifier sil ny a pas besoin de rajouter un salt
    }
    private void isNotificationContent(View view){
        activate_notifications = (Switch) view.findViewById(R.id.activate_notifications);
        /** A REMPLIR **/
    }

    private void setSpinnerContent(View view){
        mySpinner = (Spinner) view.findViewById(R.id.spinner_notifications);

        List<String> list = new ArrayList<String>();
        list.add("1 jour");
        list.add("2 jours");
        list.add("3 jours");
        list.add("5 jours");
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
