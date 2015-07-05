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

import com.yacorso.nowaste.R;

/**
 * Created by quentin on 05/07/15.
 */
public class SettingsFragment extends BaseFragment {

    public SettingsFragment() {
    }

    public static SettingsFragment newInstance(){
        return new SettingsFragment();
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_settings;
    }


}
