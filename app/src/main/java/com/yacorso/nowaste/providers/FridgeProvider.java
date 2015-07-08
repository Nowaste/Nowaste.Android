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

import com.yacorso.nowaste.NowasteApplication;
import com.yacorso.nowaste.dao.FridgeDao;
import com.yacorso.nowaste.events.ApiErrorEvent;
import com.yacorso.nowaste.events.FridgesLoadedEvent;
import com.yacorso.nowaste.events.LoadFridgesEvent;
import com.yacorso.nowaste.models.FoodList;
import com.yacorso.nowaste.models.User;
import com.yacorso.nowaste.utils.LogUtil;
import com.yacorso.nowaste.data.NowasteApi;
import com.yacorso.nowaste.models.Food;
import com.yacorso.nowaste.models.Fridge;

import java.util.List;

import de.greenrobot.event.EventBus;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class FridgeProvider extends Provider<Fridge, Long> {

    FridgeDao mFridgeDao = new FridgeDao();

    public FridgeProvider() {
        super();
        /**
         * Register all event on this service
         */
        EventBus.getDefault().register(this);
    }
    public FridgeProvider(NowasteApi api) {
        super(api);
        /**
         * Register all event on this service
         */
        EventBus.getDefault().register(this);
    }

    public void onEvent(LoadFridgesEvent event){

        mApi.getAllFridges(new Callback<List<Fridge>>() {
            @Override
            public void success(List<Fridge> fridges, Response response) {
                EventBus.getDefault().post(new FridgesLoadedEvent(fridges));
            }

            @Override
            public void failure(RetrofitError error) {
                EventBus.getDefault().post(new ApiErrorEvent(error));
            }
        });
    }

    @Override
    public void create(Fridge item) {
        if(isCreatable(item)){
            mFridgeDao.create(item);
        }
    }

    @Override
    public void update(Fridge item) {
        if(isCreatable(item)){
            mFridgeDao.update(item);
        }
    }

    @Override
    public void delete(Fridge item) {
        mFridgeDao.delete(item);
    }

    @Override
    public Fridge get(Long id) {
        Fridge fridge = mFridgeDao.get(id);

        return fridge;
    }

    @Override
    public List<Fridge> all() {

        List<Fridge> fridges = mFridgeDao.all();

        return fridges;
    }


    public Fridge getCurrentFridge(User user){
        Fridge fridge=null;
        if( user != null){
            if( user.getFridges().size() > 0){
                fridge = user.getFridges().get(0);
            }
        }

        return fridge;
    }

    public List<Food> getFoods(long fridgeId){

        FoodList foodList = mFridgeDao.get(fridgeId);
        return foodList.getFoods();
    }

    public List<Food> getFoods(int fridgeId){
        List<Food> foods = null;

        NowasteApi nowasteApi = NowasteApi.ApiInstance.getInstance();
        nowasteApi.getFoodsFromFridge(fridgeId, new Callback<List<Food>>() {
            @Override
            public void success(List<Food> foods, Response response) {

            }

            @Override
            public void failure(RetrofitError error) {

            }
        });


        return foods;
    }

    private boolean isCreatable(Fridge item){
        boolean isCreatable = false;

        if (!item.isEmpty()) {
            isCreatable = true;
        } else {
            LogUtil.LOGE(this, "item is empty !");
        }

        return isCreatable;
    }
}
