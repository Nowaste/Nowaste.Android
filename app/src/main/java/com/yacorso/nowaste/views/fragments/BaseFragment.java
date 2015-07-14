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
import android.support.annotation.LayoutRes;
import android.support.annotation.StringRes;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yacorso.nowaste.R;
import com.yacorso.nowaste.events.SetTitleEvent;
import com.yacorso.nowaste.views.activities.DrawerActivity;

import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

/**
 * Created by quentin on 15/06/15.
 */
public abstract class BaseFragment extends DialogFragment {

    View mRootView ;

    public DrawerActivity getDrawerActivity() {
        return (DrawerActivity) super.getActivity();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mRootView = inflater.inflate(getLayout(), container, false);
        ButterKnife.bind(this, mRootView);
        //EventBus.getDefault().post(new SetTitleEvent(getResources().getString(getTitle())));
        return mRootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public @StringRes int getTitle(){
        return R.string.app_name;
    }

    protected abstract @LayoutRes int getLayout();
}
