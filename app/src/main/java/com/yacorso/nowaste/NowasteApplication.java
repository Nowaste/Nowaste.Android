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
import com.yacorso.nowaste.models.CustomList;
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


    private static User sCurrentUser;

    @Override
    public void onCreate() {
        super.onCreate();

        FlowManager.init(this);

        mFridgeProvider = new FridgeProvider();
        mFoodProvider = new FoodProvider();
        EventBus.getDefault().register(this);

        UserProvider userService = new UserProvider();
        List<User> users = userService.all();


        /**
         * Testing part when data is empty
         */
        User user;
        if (users.size() == 0) {
            user = new User();
            user.setEmail("toto.albert@nowaste.fr");
            user.setFirstName("Toto");
            user.setLastName("Albert");
            userService.create(user);
        } else {
            user = users.get(0);
        }

        if (user.getFridges().size() == 0) {
            Fridge f = new Fridge();

            f.setName("Default fridge");
            f.setUser(user);

            FridgeProvider fridgeProvider = new FridgeProvider();
            fridgeProvider.create(f);
            user.addFridge(f);
        }

        if (user.getCustomLists().size() == 0) {
            CustomList customList = new CustomList();

            customList.setName("Ma liste");
            customList.setUser(user);

            CustomListProvider customListProvider = new CustomListProvider();
            customListProvider.create(customList);
            user.addCustomList(customList);
        }
        /***/


        NowasteApplication.setCurrentUser(user);

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

    public static void setCurrentUser(User currentUser) {
        sCurrentUser = currentUser;
    }

    public static User getCurrentUser() {
        return sCurrentUser;
    }

}
