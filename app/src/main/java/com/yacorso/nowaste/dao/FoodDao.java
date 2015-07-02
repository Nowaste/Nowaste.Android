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

package com.yacorso.nowaste.dao;


import com.raizlabs.android.dbflow.runtime.TransactionManager;
import com.raizlabs.android.dbflow.runtime.transaction.BaseTransaction;
import com.raizlabs.android.dbflow.runtime.transaction.TransactionListener;
import com.raizlabs.android.dbflow.runtime.transaction.UpdateTransaction;
import com.raizlabs.android.dbflow.runtime.transaction.process.InsertModelTransaction;
import com.raizlabs.android.dbflow.runtime.transaction.process.ProcessModelInfo;
import com.raizlabs.android.dbflow.runtime.transaction.process.SaveModelTransaction;
import com.raizlabs.android.dbflow.runtime.transaction.process.UpdateModelListTransaction;
import com.raizlabs.android.dbflow.sql.builder.Condition;
import com.raizlabs.android.dbflow.sql.language.Delete;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.yacorso.nowaste.events.FoodCreatedEvent;
import com.yacorso.nowaste.models.Food;
import com.yacorso.nowaste.models.Food$Table;
import com.yacorso.nowaste.models.FoodFridge;
import com.yacorso.nowaste.models.FoodFridge$Table;
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
     *
     * @param item
     */
    @Override
    public void create(final Food item) {
        insert(item, TYPE_CREATE);
    }

    /**
     * Update food in database
     *
     * @param item
     */
    @Override
    public void update(Food item) {
        this.insert(item, TYPE_UPDATE);
    }

    /**
     * Delete food in database
     *
     * @param item
     */
    @Override
    public void delete(Food item) {
        if(item.hasFoodFridge()){
            new Delete().from(FoodFridge.class).where(
                    Condition.column(FoodFridge$Table.ID).is(item.getFoodFridge().getId())
            ).query();
        }

        new Delete().from(Food.class).where(
                Condition.column(Food$Table.ID).is(item.getId())).query();
    }

    /**
     * Get food from database
     *
     * @param id
     * @return Food food
     */
    @Override
    public Food get(Long id) {
        return new Select().from(Food.class)
                .where(Condition.column(Food$Table.ID).is(id)).querySingle();
    }


    /**
     * Get all foods from database
     *
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
     *
     * @param item
     */
    public void insert(final Food item, final int type) {
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

                        if (type == TYPE_CREATE) {
                            TransactionManager.getInstance().addTransaction(
                                    new SaveModelTransaction<>(processModelInfoFood));
                        } else if (type == TYPE_UPDATE) {
                            TransactionManager.getInstance().addTransaction(
                                    new UpdateModelListTransaction<>(processModelInfoFood));
                        }

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


            if (type == TYPE_CREATE) {
                TransactionManager.getInstance().addTransaction(
                        new SaveModelTransaction<>(processModelInfoFoodFridge));
            } else if (type == TYPE_UPDATE) {
                TransactionManager.getInstance().addTransaction(
                        new UpdateModelListTransaction<>(processModelInfoFoodFridge));
            }

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
     *
     * @param foods
     */
    public void insert(List<Food> foods) {
        for (Food food : foods) {
            int type = TYPE_CREATE;

            if (food.getId() > 0) {
                type = TYPE_UPDATE;
            }

            insert(food, type);
        }
    }
}
