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

package com.yacorso.nowaste.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.yacorso.nowaste.R;
import com.yacorso.nowaste.models.User;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by quentin on 18/07/15.
 */
public class ConfigurationUtil {


    protected Context mContext = null;
    protected User mUser = new User();
    private static final ConfigurationUtil instance = new ConfigurationUtil();

    public static ConfigurationUtil getInstance() {
        return instance;
    }

    private ConfigurationUtil() {

    }

    public SharedPreferences getPreferences() {
        if (mContext != null) {
            return mContext.getSharedPreferences(mContext.getString(R.string.key_no_waste), Context.MODE_PRIVATE);
        }
        return null;
    }

    public Date getLastSync() {
        SharedPreferences sp = getPreferences();
        Calendar calendar = new GregorianCalendar(1992,Calendar.OCTOBER,3);
        Date date = calendar.getTime();

        if (sp != null) {
            Long dateLong = sp.getLong(mContext.getString(R.string.key_last_sync), -1);

            if(dateLong == -1){
                setLastSync(date);
            }else{
                calendar.setTimeInMillis(dateLong);
                date = calendar.getTime();
            }
        }

        return date;
    }

    public void setLastSync(Date date) {
        SharedPreferences sp = getPreferences();
        if (sp != null) {
            SharedPreferences.Editor editor = sp.edit();
            editor.putLong(mContext.getString(R.string.key_last_sync), date.getTime());
            editor.commit();
        }
    }


    public Context getContext() {
        return mContext;
    }

    public void setContext(Context context) {
        mContext = context;
    }

    public User getCurrentUser() {
        return mUser;
    }

    public void setCurrentUser(User user) {
        mUser = user;
    }
}
