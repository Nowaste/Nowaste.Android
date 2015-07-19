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
    }

    public FoodProvider(NowasteApi api) {
        super(api);
    }

    @Override
    public void create(Food item) {
        mFoodDao.create(item);
    }

    @Override
    public void update(Food item) {
        mFoodDao.update(item);
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
//            webservice.getFridgeFromCache(id, new Callback<Fridge>() {
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
}
