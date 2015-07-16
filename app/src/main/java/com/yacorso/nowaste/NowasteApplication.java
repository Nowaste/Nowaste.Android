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
import com.yacorso.nowaste.events.CustomListCreatedEvent;
import com.yacorso.nowaste.events.DatabaseReadyEvent;
import com.yacorso.nowaste.events.FridgeCreatedEvent;
import com.yacorso.nowaste.events.UserCreatedEvent;
import com.yacorso.nowaste.models.CustomList;
import com.yacorso.nowaste.models.Food;
import com.yacorso.nowaste.models.Fridge;
import com.yacorso.nowaste.models.User;
import com.yacorso.nowaste.providers.CustomListProvider;
import com.yacorso.nowaste.providers.FoodProvider;
import com.yacorso.nowaste.providers.FridgeProvider;
import com.yacorso.nowaste.providers.UserProvider;
import com.yacorso.nowaste.utils.LogUtil;

import java.util.List;

import de.greenrobot.event.EventBus;


public class NowasteApplication extends Application {

    private FridgeProvider mFridgeProvider;
    private FoodProvider mFoodProvider;

    public static User sCurrentUser;

    @Override
    public void onCreate() {
        super.onCreate();

        FlowManager.init(this);

        mFridgeProvider = new FridgeProvider();
        mFoodProvider = new FoodProvider();
        EventBus.getDefault().register(this);

        UserProvider userProvider = new UserProvider();
        List<User> users = userProvider.all();

        /**
         * Testing part when data is empty
         */
        User user;
        if (users.size() == 0) {
            user = new User();
            user.setEmail("toto.albert@nowaste.fr");
            user.setFirstName("Toto");
            user.setLastName("Albert");
            userProvider.create(user);
            //Will throw UserCreatedEvent
        } else {
            user = users.get(0);
            addFridgeToUserIfNone(user);
        }
    }

    public void onEvent(UserCreatedEvent event) {
        User user = event.getUser();
        addFridgeToUserIfNone(user);
    }

    private void addFridgeToUserIfNone (User user) {
        if (user.getFridges().size() == 0) {
            FridgeProvider fridgeProvider = new FridgeProvider();
            Fridge f = new Fridge();

            f.setName("Default fridge");
            f.setUser(user);
            fridgeProvider.create(f);
            //Will throw FridgeCreatedEvent
        }
        else {
            addCustomListToUserIfNone(user);
        }
    }

    public void onEvent(FridgeCreatedEvent event) {
        User user = event.getFridge().getUser();
        addCustomListToUserIfNone(user);
    }

    private void addCustomListToUserIfNone (User user) {
        if (user.getCustomLists().size() == 0) {
            CustomListProvider customListProvider = new CustomListProvider();
            CustomList customList = new CustomList();

            customList.setName("Ma liste");
            customList.setUser(user);
            customListProvider.create(customList);
            //Will throw CustomListCreatedEvent
        }
        else {
            launchDataBaseReadyEvent(user);
        }
    }

    public void onEvent(CustomListCreatedEvent event) {
        User user = event.getCustomList().getUser();
        launchDataBaseReadyEvent(user);
    }

    private void launchDataBaseReadyEvent (User user) {
        EventBus.getDefault().postSticky(new DatabaseReadyEvent(user));
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
