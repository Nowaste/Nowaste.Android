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

package com.yacorso.nowaste.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootCompletedReceiver extends BroadcastReceiver {
    public BootCompletedReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            new AlarmReceiver().setAlarm(context);
        }
    }
}
