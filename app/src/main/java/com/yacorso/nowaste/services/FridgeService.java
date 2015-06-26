/*
 * Copyright (c) 2015.
 *
 * "THE BEER-WARE LICENSE" (Revision 42):
 * Quentin Bontemps <q.bontemps@gmail>  , Florian Garnier <reventlov@tuta.io>
 * and Marjorie Déboté <marjorie.debote@free.com> wrote this file.
 * As long as you retain this notice you can do whatever you want with this stuff.
 * If we meet some day, and you think this stuff is worth it, you can buy me a beer in return.
 *
 * No Waste team
 */

package com.yacorso.nowaste.services;

import com.yacorso.nowaste.NowasteApplication;
import com.yacorso.nowaste.dao.FridgeDao;
import com.yacorso.nowaste.events.ApiErrorEvent;
import com.yacorso.nowaste.events.FridgesLoadedEvent;
import com.yacorso.nowaste.events.LoadFridgesEvent;
import com.yacorso.nowaste.models.User;
import com.yacorso.nowaste.utils.LogUtil;
import com.yacorso.nowaste.data.NowasteApi;
import com.yacorso.nowaste.models.Food;
import com.yacorso.nowaste.models.Fridge;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class FridgeService extends Service<Fridge, Long> {

    FridgeDao mFridgeDao = new FridgeDao();

    public FridgeService() {
        super();
        /**
         * Register all event on this service
         */
        EventBus.getDefault().register(this);
    }
    public FridgeService(NowasteApi api) {
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
            if(true){
                mFridgeDao.create(item);
            }
        }
    }

    @Override
    public void update(Fridge item) {
        if(isCreatable(item)){
            if(true){
                mFridgeDao.update(item);
            }
        }
    }

    @Override
    public void delete(Fridge item) {
        if(true){
            mFridgeDao.delete(item);
        }
    }

    @Override
    public Fridge get(Long id) {
        Fridge fridge = null;

        if(true){
            fridge = mFridgeDao.get(id);
        }else{

        }

        return fridge;
    }

    @Override
    public List<Fridge> all() {

        List<Fridge> fridges = new ArrayList<Fridge>();

        if(true){

            fridges = mFridgeDao.all();
        }

        return fridges;
    }


    public Fridge getCurrentFridge(){
        User currentUser = NowasteApplication.getCurrentUser();
        Fridge fridge=null;
        if( currentUser != null){
            if( currentUser.getFridges().size() > 0){
                fridge = currentUser.getFridges().get(0);
            }
        }

        return fridge;
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
