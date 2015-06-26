package com.yacorso.nowaste.services;

import com.yacorso.nowaste.dao.FoodDao;
import com.yacorso.nowaste.events.ApiErrorEvent;
import com.yacorso.nowaste.events.LoadFoodsEvent;
import com.yacorso.nowaste.models.Fridge;
import com.yacorso.nowaste.utils.LogUtil;
import com.yacorso.nowaste.data.NowasteApi;
import com.yacorso.nowaste.models.Food;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * FoodService
 */
public class FoodService extends Service<Food, Long> {

    /**
     * Food dao
     */
    FoodDao mFoodDao = new FoodDao();

    public FoodService() {
        super();
        /**
         * Register all event on this service
         */
        EventBus.getDefault().register(this);
    }

    public FoodService(NowasteApi api) {
        super(api);
        /**
         * Register all event on this service
         */
        EventBus.getDefault().register(this);
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
        if (true) {
            mFoodDao.delete(item);
        } else {

        }
    }

    @Override
    public Food get(Long id) {

        Food food = null;

        if (true) {
            food = mFoodDao.get(id);
        } else {

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

        if (true) {
            foods = mFoodDao.all();
        } else {

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
