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


import com.raizlabs.android.dbflow.sql.builder.Condition;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.raizlabs.android.dbflow.structure.AsyncModel;
import com.raizlabs.android.dbflow.structure.Model;
import com.yacorso.nowaste.events.FoodCreatedEvent;
import com.yacorso.nowaste.events.FoodDeletedEvent;
import com.yacorso.nowaste.events.FoodUpdatedEvent;
import com.yacorso.nowaste.models.CustomList;
import com.yacorso.nowaste.models.Food;
import com.yacorso.nowaste.models.Food$Table;
import com.yacorso.nowaste.models.FoodFridge;
import com.yacorso.nowaste.models.FoodList;
import com.yacorso.nowaste.models.Fridge;

import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * FoodDao
 * Relation with database
 */
public class FoodDao extends Dao<Food, Long> {

    int type;

    /**
     * Create item in database
     *
     * @param item
     */
    public void create(final Food item) {
        type = TYPE_CREATE;
        transact(item);
    }

    /**
     * Update item in database
     *
     * @param item
     */
    public void update(Food item) {
        type = TYPE_UPDATE;
        transact(item);
    }

    public void transact(final Food item) {
        final AsyncModel.OnModelChangedListener callback = new AsyncModel.OnModelChangedListener() {
            @Override
            public void onModelChanged(Model model) {
                FoodFridge foodFridge = (FoodFridge) model;
                transactFood(item, foodFridge);
            }
        };

        if (item.hasFoodFridge()) {
            FoodFridge foodFridge = item.getFoodFridge();
            if (type == TYPE_CREATE) {
                foodFridge.async().withListener(callback).save();
            }
            else if (type == TYPE_UPDATE) {
                foodFridge.async().withListener(callback).update();
            }
        }
        else {
            transactFood(item, null);
        }
    }

    private void transactFood (final Food item, FoodFridge foodFridge) {
        final AsyncModel.OnModelChangedListener callback = new AsyncModel.OnModelChangedListener() {
            @Override
            public void onModelChanged(Model model) {
                Food food = (Food) model;
                transactFoodList(food);
            }
        };

        if (type == TYPE_CREATE) {
            item.setFoodFridge(foodFridge);
            item.async().withListener(callback).save();
        }
        else if (type == TYPE_UPDATE) {
            item.setFoodFridge(foodFridge);
            item.async().withListener(callback).update();
        }
    }

    private void transactFoodList (Food food) {
        if (food.hasFridge()) {
            Fridge fridge = food.getFridge();
            if (type == TYPE_CREATE) {
                fridge.addFood(food);
            }
            fridge.async().update();
        }
        if (food.hasCustomList()) {
            CustomList customList = food.getCustomList();
            if (type == TYPE_CREATE) {
                customList.addFood(food);
            }
            customList.async().update();
        }

        if (type == TYPE_CREATE) {
            EventBus.getDefault().post(new FoodCreatedEvent(food));
        } else if (type == TYPE_UPDATE) {
            EventBus.getDefault().post(new FoodUpdatedEvent(food));
        }
    }

    /**
     * Delete item in database
     *
     * @param item
     */
    public void delete(Food item) {
        type = TYPE_DELETE;
        if (item.hasFridge()) {
            Fridge fridge = item.getFridge();
            fridge.removeFood(item);
            fridge.async().update();
        }
        if (item.hasCustomList()) {
            CustomList customList = item.getCustomList();
            customList.removeFood(item);
            customList.async().update();
        }

        final AsyncModel.OnModelChangedListener callback = new AsyncModel.OnModelChangedListener() {
            @Override
            public void onModelChanged(Model model) {
                EventBus.getDefault().post(new FoodDeletedEvent());
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
    public Food get(Long id) {
        /**
         * This query is done without transactionManager
         * because you can't return the value
         */
        return new Select()
                .from(Food.class)
                .where(Condition.column(Food$Table.ID).is(id))
                .querySingle();
    }


    /**
     * Get all foods from database
     *
     * @return List<Food> foods
     */
    @Override
    public List<Food> all() {
        return new Select().from(Food.class).queryList();
    }
}
