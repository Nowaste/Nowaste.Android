package com.yacorso.nowaste;

import android.app.Application;

import com.raizlabs.android.dbflow.config.FlowManager;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;
import com.yacorso.nowaste.bus.BusProvider;
import com.yacorso.nowaste.events.ApiErrorEvent;
import com.yacorso.nowaste.models.Fridge;
import com.yacorso.nowaste.models.User;
import com.yacorso.nowaste.services.FoodService;
import com.yacorso.nowaste.services.FridgeService;
import com.yacorso.nowaste.services.UserService;
import com.yacorso.nowaste.utils.LogUtil;
import com.yacorso.nowaste.webservice.NowasteApi;

import java.util.List;


public class NowasteApplication extends Application {

    private FridgeService mFridgeService;
    private FoodService mFoodService;
    private Bus mBus = BusProvider.getInstance();


    private static User sCurrentUser ;

    @Override
    public void onCreate() {
        super.onCreate();

        FlowManager.init(this);

        mFridgeService = new FridgeService(mBus);
        mFoodService = new FoodService(mBus);
        mBus.register(mFridgeService);
        mBus.register(mFoodService);

        mBus.register(this);

        UserService userService = new UserService(mBus);
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

            FridgeService fridgeService = new FridgeService(mBus);
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

        mBus.unregister(mFridgeService);
        mBus.unregister(mFoodService);

        mBus.unregister(this);
    }

    @Subscribe
    public void onApiError(ApiErrorEvent event) {
        LogUtil.LOGD(this, "###API-FAIL### " + event.getErrorMessage());
    }

    public static void setCurrentUser(User currentUser){
        sCurrentUser = currentUser;
    }

    public static User getCurrentUser(){
        return sCurrentUser;
    }

}
