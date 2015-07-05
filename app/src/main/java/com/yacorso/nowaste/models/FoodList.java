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

package com.yacorso.nowaste.models;

import com.google.gson.annotations.Expose;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ForeignKey;
import com.raizlabs.android.dbflow.annotation.ForeignKeyReference;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.structure.cache.BaseCacheableModel;
import com.raizlabs.android.dbflow.structure.container.ForeignKeyContainer;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public abstract class FoodList extends BaseCacheableModel {

    /**
     * Attributes
     */

    @Column
    @PrimaryKey(autoincrement = true)
    @Expose
    protected long id;

    @Column
    @Expose
    protected String name;

    protected List<Food> foods;


    @Column
    @ForeignKey(
            references = {@ForeignKeyReference(columnName = "user_id",
                    columnType = Long.class,
                    foreignColumnName = "id")},
            saveForeignKeyModel = false
    )
    @Expose
    protected ForeignKeyContainer<User> user;


    /**
     * Functions
     */
    public FoodList() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ForeignKeyContainer<User> getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = new ForeignKeyContainer<>(User.class);
        Map<String, Object> keys = new LinkedHashMap<>();
        keys.put(User$Table.ID, user.id);
        this.user.setData(keys);
        this.user.setModel(user);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
//        for (Food food : foodList) {
//            stringBuilder.append(food.toString());
//            stringBuilder.append("\n");
//        }
        stringBuilder.append(name);
        return stringBuilder.toString();
    }

    public boolean isEmpty() {
        boolean isEmpty = false;

        if (id != 0) {
            isEmpty = false;
        }

        return isEmpty;
    }

    public void addFood(Food food){
        this.foods.add(food);
    }

}
