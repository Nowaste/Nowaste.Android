package com.yacorso.nowaste;

import android.app.Application;
import com.raizlabs.android.dbflow.config.FlowManager;

public class NowasteApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FlowManager.init(this);
    }

}
