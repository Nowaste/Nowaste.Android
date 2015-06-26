package com.yacorso.nowaste.dao;


import com.raizlabs.android.dbflow.runtime.TransactionManager;
import com.raizlabs.android.dbflow.runtime.transaction.BaseTransaction;
import com.raizlabs.android.dbflow.runtime.transaction.TransactionListener;
import com.raizlabs.android.dbflow.runtime.transaction.process.InsertModelTransaction;
import com.raizlabs.android.dbflow.runtime.transaction.process.ProcessModelInfo;
import com.raizlabs.android.dbflow.runtime.transaction.process.SaveModelTransaction;
import com.raizlabs.android.dbflow.sql.builder.Condition;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.yacorso.nowaste.events.FoodCreatedEvent;
import com.yacorso.nowaste.models.Food;
import com.yacorso.nowaste.models.Food$Table;
import com.yacorso.nowaste.models.FoodFridge;
import com.yacorso.nowaste.models.FoodList;

import com.yacorso.nowaste.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * FoodDao
 * Relation with database
 */
public class FoodDao extends Dao<Food, Long> {

    /**
     * Transaction listener for FoodFridge
     */
    TransactionListener mResultReceiverFoodFridge;

    /**
     * Transaction listener for Food
     */
    TransactionListener mResultReceiverFood;


    /**
     * Create a food in database
     * @param item
     */
    @Override
    public void create(final Food item) {

        mResultReceiverFoodFridge = new TransactionListener() {
            @Override
            public void onResultReceived(Object o) {

                /**
                 * When foodFridge was created,
                 * Create food
                 */
                ArrayList<FoodList> result = (ArrayList<FoodList>) o;

                if (!result.isEmpty()) {
                    o = result.get(0);

                    if (o.getClass() == FoodFridge.class && !((FoodFridge) o).isEmpty()) {

                        item.setFoodFridge((FoodFridge) o);
                        ProcessModelInfo<Food> processModelInfoFood =
                                ProcessModelInfo.withModels(item)
                                        .result(mResultReceiverFood);
                        TransactionManager.getInstance().addTransaction(
                                new SaveModelTransaction<>(processModelInfoFood));

                    }
                    LogUtil.LOGD(this, "onResultReceived -- FoodFridge");
                }
            }

            @Override
            public boolean onReady(BaseTransaction baseTransaction) {
                return baseTransaction.onReady();
            }

            @Override
            public boolean hasResult(BaseTransaction baseTransaction, Object o) {
                return true;
            }
        };


        mResultReceiverFood = new TransactionListener() {

            @Override
            public void onResultReceived(Object o) {
                /**
                 * When food was created, push then FoodCreatedEvent
                 * For all listeners
                 */
                EventBus.getDefault().post(new FoodCreatedEvent());
                LogUtil.LOGD(this, "onResultReceived -- Food");
            }

            @Override
            public boolean onReady(BaseTransaction baseTransaction) {
                return baseTransaction.onReady();
            }

            @Override
            public boolean hasResult(BaseTransaction baseTransaction, Object o) {
                return true;
            }
        };

        /**
         * If food has a Fridge and Food Fridge
         * Create food with foodFridge and fridge infos
         */
        if (item.hasFridge() && item.hasFoodFridge()) {

            LogUtil.LOGD(this, "HasFoodFridge");

            /**
             * Set DBFlow Transaction foodFridge
             */
            ProcessModelInfo<FoodFridge> processModelInfoFoodFridge =
                    ProcessModelInfo.withModels(item.getFoodFridge())
                            .result(mResultReceiverFoodFridge);
            TransactionManager.getInstance().addTransaction(
                    new SaveModelTransaction<>(processModelInfoFoodFridge));

        }
        /**
         * If food has a custom
         */
        else if (item.hasCustomList()) {
            /**
             * Set DBFlow Transaction
             */
            ProcessModelInfo<Food> processModelInfoFood =
                    ProcessModelInfo.withModels(item)
                            .result(mResultReceiverFood);
            TransactionManager.getInstance().addTransaction(
                    new SaveModelTransaction<>(processModelInfoFood));
        } else {
            LogUtil.LOGE(this, item.getName() + " hasn't Fridge and Custom List !");
        }
    }

    /**
     * Update food in database
     * @param item
     */
    @Override
    public void update(Food item) {

    }

    /**
     *  Delete food in database
     * @param item
     */
    @Override
    public void delete(Food item) {

    }

    /**
     * Get food from database
     * @param id
     * @return Food food
     */
    @Override
    public Food get(Long id) {
        return new Select().from(Food.class)
                .where(Condition.column(Food$Table.ID).is(id)).querySingle();
    }


    /**
     *  Get all foods from database
     * @return List<Food> foods
     */
    @Override
    public List<Food> all() {
        return new Select().all().from(Food.class).queryList();
    }


    /**
     * Insert food in database
     * If food object already exists, update this
     * Else food object is created
     * @param item
     */
    public void insert(final Food item){
        mResultReceiverFoodFridge = new TransactionListener() {
            @Override
            public void onResultReceived(Object o) {

                ArrayList<FoodList> result = (ArrayList<FoodList>) o;

                if (!result.isEmpty()) {
                    o = result.get(0);

                    if (o.getClass() == FoodFridge.class && !((FoodFridge) o).isEmpty()) {

                        item.setFoodFridge((FoodFridge) o);
                        ProcessModelInfo<Food> processModelInfoFood =
                                ProcessModelInfo.withModels(item)
                                        .result(mResultReceiverFood);
                        TransactionManager.getInstance().addTransaction(
                                new InsertModelTransaction<>(processModelInfoFood));

                    }
                    LogUtil.LOGD(this, "onResultReceived -- FoodFridge");
                }

            }

            @Override
            public boolean onReady(BaseTransaction baseTransaction) {
                return baseTransaction.onReady();
            }

            @Override
            public boolean hasResult(BaseTransaction baseTransaction, Object o) {
                return true;
            }
        };


        mResultReceiverFood = new TransactionListener() {

            @Override
            public void onResultReceived(Object o) {
                EventBus.getDefault().post(new FoodCreatedEvent());
                LogUtil.LOGD(this, "onResultReceived -- Food");
            }

            @Override
            public boolean onReady(BaseTransaction baseTransaction) {
                return baseTransaction.onReady();
            }

            @Override
            public boolean hasResult(BaseTransaction baseTransaction, Object o) {
                return true;
            }
        };


        if (item.hasFridge() && item.hasFoodFridge()) {

            /**
             * Save Food Fridge
             */
            LogUtil.LOGD(this, "HasFoodFridge");

            /**
             * Set DBFlow Transaction
             */
            ProcessModelInfo<FoodFridge> processModelInfoFoodFridge =
                    ProcessModelInfo.withModels(item.getFoodFridge())
                            .result(mResultReceiverFoodFridge);
            TransactionManager.getInstance().addTransaction(
                    new InsertModelTransaction<>(processModelInfoFoodFridge));

        } else if (item.hasCustomList()) {
            ProcessModelInfo<Food> processModelInfoFood =
                    ProcessModelInfo.withModels(item)
                            .result(mResultReceiverFood);
            TransactionManager.getInstance().addTransaction(
                    new InsertModelTransaction<>(processModelInfoFood));
        } else {
            LogUtil.LOGE(this, item.getName() + " hasn't Fridge and Custom List !");
        }
    }

    /**
     * Insert foods with list of food
     * @param foods
     */
    public void insert(List<Food> foods){
        for(Food food : foods){
            insert(food);
        }
    }
}
