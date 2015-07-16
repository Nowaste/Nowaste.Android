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
import com.raizlabs.android.dbflow.runtime.transaction.TransactionListenerAdapter;
import com.raizlabs.android.dbflow.runtime.transaction.process.ProcessModelInfo;
import com.raizlabs.android.dbflow.runtime.transaction.process.ProcessModelTransaction;
import com.raizlabs.android.dbflow.runtime.transaction.process.UpdateModelListTransaction;
import com.raizlabs.android.dbflow.sql.builder.Condition;
import com.raizlabs.android.dbflow.sql.language.Select;

import com.raizlabs.android.dbflow.structure.AsyncModel;
import com.raizlabs.android.dbflow.structure.Model;
import com.yacorso.nowaste.events.FridgeCreatedEvent;
import com.yacorso.nowaste.events.FridgeDeletedEvent;
import com.yacorso.nowaste.events.FridgeUpdatedEvent;
import com.yacorso.nowaste.models.Food;
import com.yacorso.nowaste.models.FoodFridge;
import com.yacorso.nowaste.models.Fridge;
import com.yacorso.nowaste.models.Fridge$Table;
import com.yacorso.nowaste.models.User;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * FridgeDao
 * Relation with database
 */
public class FridgeDao extends Dao<Fridge, Long> {

    int type;

    /**
     * Insert item in database
     *
     * @param item
     * @return
     */
    public void create(final Fridge item) {
        type = TYPE_CREATE;
        transact(item);
    }

    /**
     * Update item in database
     *
     * @param item
     * @return
     */
    public void update(Fridge item) {
        type = TYPE_UPDATE;
        transact(item);
    }

    public void transact(final Fridge item) {
        final AsyncModel.OnModelChangedListener callback = new AsyncModel.OnModelChangedListener() {
            @Override
            public void onModelChanged(Model model) {
                Fridge fridge = (Fridge) model;

                User user = fridge.getUser();
                if (type == TYPE_CREATE) {
                    user.addFridge(fridge);
                }
                user.async().update();

                if (type == TYPE_CREATE) {
                    EventBus.getDefault().post(new FridgeCreatedEvent(fridge));
                } else if (type == TYPE_UPDATE) {
                    EventBus.getDefault().post(new FridgeUpdatedEvent(fridge));
                }
            }
        };

        if (type == TYPE_CREATE) {
            item.async().withListener(callback).save();
        }
        else if (type == TYPE_UPDATE){
            TransactionListenerAdapter resultFoods = new TransactionListenerAdapter() {
                @Override
                public void onResultReceived(Object o) {
                    item.async().withListener(callback).update();
                }
            };
            updateFood(item.getFoods(), resultFoods);
        }
    }

    private void updateFood(final List<Food> foods, final TransactionListenerAdapter result) {
        TransactionListenerAdapter callback = new TransactionListenerAdapter() {
            @Override
            public void onResultReceived(Object o) {
                ProcessModelInfo processModelInfo = ProcessModelInfo.withModels(foods).result(result);
                ProcessModelTransaction transaction = new UpdateModelListTransaction(processModelInfo);
                TransactionManager.getInstance().addTransaction(transaction);
            }
        };

        final List<FoodFridge> foodFridges = new ArrayList<>();
        for (Food food: foods) {
            foodFridges.add(food.getFoodFridge());
        }

        ProcessModelInfo processModelInfo = ProcessModelInfo.withModels(foodFridges).result(callback);
        ProcessModelTransaction transaction = new UpdateModelListTransaction(processModelInfo);
        TransactionManager.getInstance().addTransaction(transaction);
    }

    /**
     * Delete item in database
     *
     * @param item
     */
    public void delete(Fridge item) {
        type = TYPE_DELETE;
        User user = item.getUser();
        user.removeFridge(item);
        user.async().update();

        final AsyncModel.OnModelChangedListener callback = new AsyncModel.OnModelChangedListener() {
            @Override
            public void onModelChanged(Model model) {
                EventBus.getDefault().post(new FridgeDeletedEvent());
            }
        };

        item.async().withListener(callback).delete();
    }

    /**
     * Get food from database
     *
     * @param id
     * @return Food food
     */
    @Override
    public Fridge get(Long id) {
        return new Select()
                .from(Fridge.class)
                .where(Condition.column(Fridge$Table.ID).is(id))
                .querySingle();
    }


    /**
     * Get all foods from database
     *
     * @return List<Food> foods
     */
    @Override
    public List<Fridge> all() {
        return new Select().from(Fridge.class).queryList();
    }
}
