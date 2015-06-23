package com.yacorso.nowaste;

import android.app.Application;

import com.raizlabs.android.dbflow.config.FlowManager;
import com.yacorso.nowaste.events.ApiErrorEvent;
import com.yacorso.nowaste.models.Fridge;
import com.yacorso.nowaste.models.User;
import com.yacorso.nowaste.services.FoodService;
import com.yacorso.nowaste.services.FridgeService;
import com.yacorso.nowaste.services.UserService;
import com.yacorso.nowaste.utils.LogUtil;
import com.yacorso.nowaste.webservice.NowasteApi;

import java.util.List;

import de.greenrobot.event.EventBus;


public class NowasteApplication extends Application {

    private FridgeService mFridgeService;
    private FoodService mFoodService;


    private static User sCurrentUser ;

    @Override
    public void onCreate() {
        super.onCreate();

        FlowManager.init(this);

        mFridgeService = new FridgeService();
        mFoodService = new FoodService();
        EventBus.getDefault().register(this);

        UserService userService = new UserService();
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
        }else{
            user = users.get(0);
        }

        if(user.getFridges().size() == 0){
            Fridge f = new Fridge();

            f.setName("Default fridge");
            f.setUser(user);

            FridgeService fridgeService = new FridgeService();
            fridgeService.create(f);
            user.addFridge(f);
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

    public static void setCurrentUser(User currentUser){
        sCurrentUser = currentUser;
    }

    public static User getCurrentUser(){
        return sCurrentUser;
    }

}
