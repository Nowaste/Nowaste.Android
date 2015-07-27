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
    Configuration maConfig;
    User monUser;
    View layout;

    public SettingsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layout = inflater.inflate(R.layout.fragment_settings, container, false);
        mRootView = super.onCreateView(inflater, container, savedInstanceState);

        maConfig = new Configuration();
        monUser = new User();

        setMailContent();
        setFirstNameContent();
        setLastNameContent();
        setPasswordContent();

        return mRootView;
    }

    private void setMailContent(){
        text_email = (EditText) layout.findViewById(R.id.text_email);
        String string_email = text_email.getText().toString();
        //monUser.setEmail(string_email);
        monUser.setEmail("marjorie.debote@free.fr");
    }
    private void setFirstNameContent(){
        text_first_name = (EditText) layout.findViewById(R.id.text_first_name);
        String string_first_name = text_first_name.getText().toString();
        //monUser.setFirstName(string_first_name);
        monUser.setFirstName("Debote");
    }
    private void setLastNameContent(){
        text_last_name = (EditText) layout.findViewById(R.id.text_last_name);
        String string_last_name = text_last_name.getText().toString();
        //monUser.setLastName(string_last_name);
        monUser.setLastName("Marjorie");
    }
    private void setPasswordContent(){
        text_password = (EditText) layout.findViewById(R.id.text_password);
        String string_password = text_password.getText().toString();
        //monUser.setPassword(string_password);
        monUser.setPassword("password");
        //verifier sil ny a pas besoin de rajouter un salt
    }
    private void isNotificationContent(){
        activate_notifications = (Switch) layout.findViewById(R.id.activate_notifications);
        maConfig.setKey("isNotificationContent");
        maConfig.setValue("true");
        /** A REMPLIR **/
    }

    public static SettingsFragment newInstance(){
        return new SettingsFragment();
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_settings;
    }


}
