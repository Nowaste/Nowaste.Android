package com.yacorso.nowaste.dao;

import android.util.Log;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.runtime.TransactionManager;
import com.raizlabs.android.dbflow.runtime.transaction.BaseTransaction;
import com.raizlabs.android.dbflow.runtime.transaction.TransactionListener;
import com.raizlabs.android.dbflow.runtime.transaction.process.InsertModelTransaction;
import com.raizlabs.android.dbflow.runtime.transaction.process.ProcessModelInfo;
import com.raizlabs.android.dbflow.runtime.transaction.process.SaveModelTransaction;
import com.raizlabs.android.dbflow.runtime.transaction.process.UpdateModelListTransaction;
import com.raizlabs.android.dbflow.sql.builder.Condition;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.yacorso.nowaste.events.FoodCreatedEvent;
import com.yacorso.nowaste.models.Food;
import com.yacorso.nowaste.models.Food$Table;
import com.yacorso.nowaste.models.FoodFridge;
import com.yacorso.nowaste.models.FoodList;
import com.yacorso.nowaste.models.Fridge;
import com.yacorso.nowaste.models.Fridge$Table;
import com.yacorso.nowaste.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;

public class FoodDao extends Dao<Food, Long> {

    TransactionListener mResultReceiverFoodFridge;
    TransactionListener mResultReceiverFood;

    @Override
    public void create(final Food item) {

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
                getBus().post(new FoodCreatedEvent());
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
                    new SaveModelTransaction<>(processModelInfoFoodFridge));

        } else if (item.hasCustomList()) {
            ProcessModelInfo<Food> processModelInfoFood =
                    ProcessModelInfo.withModels(item)
                            .result(mResultReceiverFood);
            TransactionManager.getInstance().addTransaction(
                    new SaveModelTransaction<>(processModelInfoFood));
        } else {
            LogUtil.LOGE(this, item.getName() + " hasn't Fridge and Custom List !");
        }
    }

    @Override
    public void update(Food item) {

    }

    @Override
    public void delete(Food item) {

    }

    @Override
    public Food get(Long id) {
        return new Select().from(Food.class)
                .where(Condition.column(Food$Table.ID).is(id)).querySingle();
    }

    @Override
    public List<Food> all() {
        return null;
    }


    /**
     *
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
                getBus().post(new FoodCreatedEvent());
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
     *
     * @param foods
     */
    public void insert(List<Food> foods){
        for(Food food : foods){
            insert(food);
        }
    }
}
