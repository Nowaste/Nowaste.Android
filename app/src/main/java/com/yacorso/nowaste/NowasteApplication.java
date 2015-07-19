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

package com.yacorso.nowaste;

import android.app.Application;

import com.raizlabs.android.dbflow.config.FlowManager;
import com.yacorso.nowaste.events.ApiErrorEvent;
import com.yacorso.nowaste.utils.LogUtil;

import de.greenrobot.event.EventBus;


public class NowasteApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FlowManager.init(this);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        FlowManager.destroy();
        EventBus.getDefault().unregister(this);
    }

    public void onEvent(ApiErrorEvent event) {
        LogUtil.LOGD(this, "###API-FAIL### " + event.getErrorMessage());
    }
}
