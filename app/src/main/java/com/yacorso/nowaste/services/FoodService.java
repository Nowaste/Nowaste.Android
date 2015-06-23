package com.yacorso.nowaste.services;

import android.content.Context;
import android.util.Log;

import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;
import com.yacorso.nowaste.NowasteApplication;
import com.yacorso.nowaste.dao.FoodDao;
import com.yacorso.nowaste.events.ApiErrorEvent;
import com.yacorso.nowaste.events.FridgesLoadedEvent;
import com.yacorso.nowaste.events.LoadFoodsEvent;
import com.yacorso.nowaste.events.LoadFridgesEvent;
import com.yacorso.nowaste.models.CustomList;
import com.yacorso.nowaste.models.FoodFridge;
import com.yacorso.nowaste.models.FoodList;
import com.yacorso.nowaste.models.Fridge;
import com.yacorso.nowaste.models.User;
import com.yacorso.nowaste.utils.LogUtil;
import com.yacorso.nowaste.webservice.NowasteApi;
import com.yacorso.nowaste.models.Food;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by quentin on 17/06/15.
 */
public class FoodService extends Service<Food, Long> {

    FoodDao mFoodDao = new FoodDao();

    public FoodService(Bus bus) {
        super(bus);
    }

    public FoodService(NowasteApi api, Bus bus) {
        super(api, bus);
    }

    @Override
    public void create(Food item) {

        if (isCreatable(item)) {
            if (true) {
                /**
                 * Create Food to database
                 */
                mFoodDao.create(item);

            } else {
                /**
                 * Create Food to webservice
                 */
            }
        }
    }

    @Override
    public void update(Food item) {
        if (isCreatable(item)) {
            if (true) {
                /**
                 * Create Food to database
                 */
                mFoodDao.update(item);
            } else {
                /**
                 * Create Food to webservice
                 */
            }
        }
    }
    @Override
    public void delete(Food item) {
        if(true){
            mFoodDao.delete(item);
        }else{

        }
    }

    @Override
    public Food get(Long id) {

        Food food = null;

        if(true){
            food = mFoodDao.get(id);
        }else{

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
        }

        return food;

    }

    @Override
    public List<Food> all() {
        List<Food> foods = new ArrayList<Food>();

        if(true){
            foods = mFoodDao.all();
        }else{

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
        }



        return foods;
    }

    private boolean isCreatable(Food item){
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

    @Subscribe
    public void onLoadFoods(LoadFoodsEvent event){

        mApi.getAllFridges(new Callback<List<Fridge>>() {
            @Override
            public void success(List<Fridge> fridges, Response response) {
//                mBus.post(new FridgesLoadedEvent(fridges));
                LogUtil.LOGD(this,"yay");
            }

            @Override
            public void failure(RetrofitError error) {
                mBus.post(new ApiErrorEvent(error));
            }
        });
    }


}
