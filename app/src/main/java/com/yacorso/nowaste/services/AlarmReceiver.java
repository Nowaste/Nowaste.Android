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

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.os.SystemClock;

import java.util.Calendar;

public class AlarmReceiver extends BroadcastReceiver{

    private AlarmManager alarmMgr;
    private PendingIntent alarmIntent;

    public AlarmReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent serviceIntent = new Intent(context, NotificationService.class);
        context.stopService(serviceIntent);
        context.startService(serviceIntent);
    }

    public void setAlarm(Context context) {
        boolean alarmUp = (PendingIntent.getBroadcast(context, 0,
                new Intent(context, AlarmReceiver.class),
                PendingIntent.FLAG_NO_CREATE) != null);

        if (!alarmUp) {
            alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(context, AlarmReceiver.class);
            alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.set(Calendar.HOUR_OF_DAY, 19);

            alarmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY, alarmIntent);


            //For tests
            //Alarm trigger every minute
        /*alarmMgr.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime() + 15 * 1000, 45*1000, alarmIntent);
        */
        }
    }
}
