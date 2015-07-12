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

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.system.ErrnoException;
import android.widget.Toast;

import com.raizlabs.android.dbflow.config.FlowManager;
import com.yacorso.nowaste.R;
import com.yacorso.nowaste.models.Food;
import com.yacorso.nowaste.models.FoodFridge;
import com.yacorso.nowaste.models.Fridge;
import com.yacorso.nowaste.models.User;
import com.yacorso.nowaste.providers.UserProvider;
import com.yacorso.nowaste.utils.LogUtil;
import com.yacorso.nowaste.views.activities.DrawerActivity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.yacorso.nowaste.utils.DateUtils.checkIfOutOfDateIsSoon;

public class NotificationService extends Service {
    NotificationManager mNM;

    public NotificationService() {
    }

    // Unique Identification Number for the Notification.
    // We use it on Notification start, and to cancel it.
    private int NOTIFICATION = R.string.local_service_started;

    @Override
    public void onCreate() {
        mNM = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        UserProvider userProvider = new UserProvider();
        List<User> users = userProvider.all();
        User user;
        FlowManager.init(getApplicationContext());
        try {
            if (users.size() == 0) { throw new Error("no user"); }

            List<Fridge> fridges = users.get(0).getFridges();
            if (fridges.size() == 0) { throw new Error("no fridge"); }

            final List<Food> foods =fridges.get(0).getFoods();
            if (fridges.size() == 0) { throw new Error("no food"); }

            List<Food> foodsSoonOutOfDate = new ArrayList<>();
            for (Food food : foods) {
                Date date = food.getFoodFridge().getOutOfDate();

                if (checkIfOutOfDateIsSoon(date)) {
                    foodsSoonOutOfDate.add(food);
                }
            }
            if (foodsSoonOutOfDate.size() == 0) { throw new Error("no food soon out of date"); }

            showNotification(
                    foodsSoonOutOfDate,
                    foodsSoonOutOfDate.size()
            );
        }
        catch(Error e) {
            stopSelf();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogUtil.LOGI("Notification", "Notification start id " + startId + ": " + intent);
        // We want this service to continue running until it is explicitly
        // stopped, so return sticky.
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        // Cancel the persistent notification.
        mNM.cancelAll();
        LogUtil.LOGI("Notification", "notification service stooped");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    /**
     * Show a notification while this service is running.
     */
    private void showNotification(List<Food> foods, int count) {
        Intent resultIntent = new Intent(this, DrawerActivity.class);
        resultIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(this,
                        1001,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        StringBuilder text = new StringBuilder();
        for (Food food : foods) {
            if (text.length() != 0) {
                text.append(", ");
            }
            text.append(Integer.toString(food.getFoodFridge().getQuantity()))
                    .append(" ")
                    .append(food.getName())
                    .append(" ");
        }

        if (foods.size() == 1 && foods.get(0).getFoodFridge().getQuantity() == 1) {
            text.append(getText(R.string.notif_text_single));
        }
        else {
            text.append(getText(R.string.notif_text_multiple));
        }

        // Set the icon, scrolling text and timestamp
        Notification notification =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.cast_ic_notification_on)
                        .setContentTitle(getText(R.string.notif_title))
                        .setContentText(text.toString())
                        .setContentIntent(resultPendingIntent)
                        .setAutoCancel(true)
                        .setNumber(count)
                        .build();

        // Send the notification.
        mNM.notify(NOTIFICATION, notification);
    }
}
