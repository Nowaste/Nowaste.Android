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

package com.yacorso.nowaste.providers;

import com.yacorso.nowaste.dao.FoodDao;
import com.yacorso.nowaste.dao.FridgeDao;
import com.yacorso.nowaste.events.LoadFoodsEvent;
import com.yacorso.nowaste.utils.LogUtil;
import com.yacorso.nowaste.data.NowasteApi;
import com.yacorso.nowaste.models.Food;

import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * FoodService
 */
public class FoodProvider extends Provider<Food, Long> {

    /**
     * Food dao
     */
    FoodDao mFoodDao = new FoodDao();

    public FoodProvider() {
        super();
        /**
         * Register all event on this service
         */
        EventBus.getDefault().register(this);
    }

    public FoodProvider(NowasteApi api) {
        super(api);
        /**
         * Register all event on this service
         */
        EventBus.getDefault().register(this);
    }

    @Override
    public void create(Food item) {
        if (isCreatable(item)) {
            mFoodDao.create(item);
        }
    }

    @Override
    public void update(Food item) {
        if (isCreatable(item)) {
            mFoodDao.update(item);
        }
    }

    @Override
    public void delete(Food item) {
        mFoodDao.delete(item);
    }

    @Override
    public Food get(Long id) {

        Food food = mFoodDao.get(id);

//            NowasteApi webservice = Nt WasteApi.ApiInstance.getInstance();
//
//            webservice.getFridge(id, new Callback<Fridge>() {
//                @Override
//                public void success(Fridge fridge, Response response) {
//
//                }
//
//                @Override
//                public void failure(RetrofitError error) {
//
//                }
//            });

        return food;

    }

    @Override
    public List<Food> all() {
        List<Food> foods = mFoodDao.all();

//        NowasteApi nowasteApi = NowasteApi.ApiInstance.getInstance();

//        noWasteApi.getAllFridges(new Callback<Fridge>() {
//            @Override
//            public void success(Fridge fridge, Response response) {
//
//            }
//
//            @Override
//            public void failure(RetrofitError error) {
//
//            }
//        });

        return foods;
    }

    private boolean isCreatable(Food item) {
        boolean isCreatable = false;

        if (!item.isEmpty()) {
            if (item.hasFridge()) {
                if (item.hasFoodFridge()) {
                    /**
                     * Food can be created
                     */
                    isCreatable = true;
                } else {
                    LogUtil.LOGE(this, item.getName() + " hasn't FoodFridge!");
                }
            } else if (item.hasCustomList()) {
                isCreatable = true;
            } else {
                LogUtil.LOGE(this, item.getName() + " hasn't Fridge!");
            }
        } else {
            LogUtil.LOGE(this, "item is empty !");
        }

        return isCreatable;
    }


    public void onEvent(LoadFoodsEvent event) {

        if (event.getFoodList() != null) {
            /**
             * TODO: Récupérer en fonction de la foodlist en paramètre
             */
        }

//        mApi.getAllFridges(new Callback<List<Fridge>>() {
//            @Override
//            public void success(List<Fridge> fridges, Response response) {
////                mBus.post(new FridgesLoadedEvent(fridges));
//                LogUtil.LOGD(this,"yay");
//            }
//
//            @Override
//            public void failure(RetrofitError error) {
//                EventBus.getDefault().post(new ApiErrorEvent(error));
//            }
//        });


    }


}
