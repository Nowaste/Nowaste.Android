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
import com.yacorso.nowaste.models.Fridge;
import com.yacorso.nowaste.utils.LogUtil;
import com.yacorso.nowaste.data.NowasteApi;
import com.yacorso.nowaste.models.Food;

import java.util.List;

import de.greenrobot.event.EventBus;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

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


        item.setFridgeId();
        final Food itemCreated = item;

        NowasteApi api = NowasteApi.ApiInstance.getInstance();
        api.createFood(itemCreated, new Callback<Food>() {
            @Override
            public void success(Food food, Response response) {
                try {
                    Food newItem = (Food) itemCreated.clone();
                    newItem.setServerId(food.getId());
                    mFoodDao.update(newItem);
                } catch (CloneNotSupportedException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                LogUtil.LOGD(this, error.toString());
            }
        });
    }

    @Override
    public void update(Food item) {
        mFoodDao.update(item);
        item.setFridgeId();

        this.sync();

        if(item.getServerId() != 0){

            Food itemUpdated = null;
            try {
                itemUpdated = (Food)item.clone();
                itemUpdated.setId(item.getServerId());
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }

            if(itemUpdated != null){

                NowasteApi api = NowasteApi.ApiInstance.getInstance();
                api.updateFood(itemUpdated.getId(),itemUpdated, new Callback<Food>() {
                    @Override
                    public void success(Food food, Response response) {

                    }

                    @Override
                    public void failure(RetrofitError error) {
                        LogUtil.LOGD(this, "## ERROR ##");
                    }
                });
            }
        }
    }

    @Override
    public void delete(Food item) {
        mFoodDao.delete(item);

        if(item.getServerId() != 0){

            Food itemDeleted = null;
            try {
                itemDeleted = (Food)item.clone();
                itemDeleted.setId(item.getServerId());
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }

            if(itemDeleted != null){

                NowasteApi api = NowasteApi.ApiInstance.getInstance();
                api.deleteFood(itemDeleted.getId(), new Callback<Food>() {
                    @Override
                    public void success(Food FOOD, Response response) {

                    }

                    @Override
                    public void failure(RetrofitError error) {

                    }
                });
            }
        }
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
}
