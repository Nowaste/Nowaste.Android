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

import android.content.ContentValues;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;

import com.google.gson.annotations.Expose;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ForeignKey;
import com.raizlabs.android.dbflow.annotation.ForeignKeyReference;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.container.ForeignKeyContainer;
import com.yacorso.nowaste.data.NowasteDatabase;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

@Table(databaseName = NowasteDatabase.NAME)
public class Food extends Model implements Parcelable {


    /**
     * Attributes
     */

    @Column
    @Expose
    @PrimaryKey(autoincrement = true)
    long id;

    @Column
    @Expose
    String name;

    @Column
    @Expose
    @Nullable
    @ForeignKey(
            references = {@ForeignKeyReference(
                    columnName = "foodfridge_id",
                    columnType = Long.class,
                    foreignColumnName = "id")},
            saveForeignKeyModel = false)
    FoodFridge foodFridge;

    @Column
    @Expose
    @ForeignKey(
            references = {@ForeignKeyReference(
                    columnName = "fridge_id",
                    columnType = Long.class,
                    foreignColumnName = "id")},
            saveForeignKeyModel = false
    )
    ForeignKeyContainer<Fridge> fridge;

    @Column
    @Expose
    @ForeignKey(
            references = {@ForeignKeyReference(
                    columnName = "customlist_id",
                    columnType = Long.class,
                    foreignColumnName = "id")},
            saveForeignKeyModel = false
    )
    ForeignKeyContainer<CustomList> customList;

    /**
     * Functions
     */

    public Food() {
    }

    public Food(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Food(String name) {
        this.name = name;
    }

    public Food(Parcel in) {
        this.id = in.readLong();
        this.name = in.readString();
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

    public FoodFridge getFoodFridge() {
        if (foodFridge == null) {
            foodFridge = new FoodFridge();
        }
        return foodFridge;
    }

    public void setFoodFridge(FoodFridge foodFridge) {
        this.foodFridge = foodFridge;
    }

    public void setFoodList(FoodList foodList) {
        if (foodList instanceof Fridge) {
            setFridge((Fridge)foodList);
        }
        else if(foodList instanceof CustomList) {
            setCustomList((CustomList) foodList);
        }
    }


    public Fridge getFridge() {
        if (fridge == null) {
            return null;
        }
        return fridge.load();
    }

    private void setFridge(Fridge fridge) {
        this.fridge = new ForeignKeyContainer<>(Fridge.class);
        Map<String, Object> keys = new LinkedHashMap<>();
        keys.put(Fridge$Table.ID, fridge.id);
        this.fridge.setData(keys);
        //this.fridge.setModel(fridge);
    }

    public CustomList getCustomList() {
        if (customList == null) {
            return null;
        }
        return customList.load();
    }

    private void setCustomList(CustomList customList) {
        this.customList = new ForeignKeyContainer<>(CustomList.class);
        Map<String, Object> keys = new LinkedHashMap<>();
        keys.put(CustomList$Table.ID, customList.id);
        this.customList.setData(keys);
        //this.customList.setModel(customList);
    }

    public ContentValues getContentValues() {
        ContentValues cV = new ContentValues();
        cV.put("name", this.name);
        return cV;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeLong(this.id);
        out.writeString(this.name);
    }

    public static final Creator<Food> CREATOR = new Creator<Food>() {
        @Override
        public Food createFromParcel(Parcel in) {
            return new Food(in);
        }

        @Override
        public Food[] newArray(int size) {
            return new Food[size];
        }
    };

    @Override
    public String toString() {
        return "Food [id =" + this.id + ", name =" + this.name + "]";
    }

    public static ArrayList<Food> getTestingArray(int j) {

        ArrayList<Food> foods = new ArrayList<Food>();

        for (int i = 0; i < j; i++) {
            Food food = new Food(i, "Aliment " + i);
            foods.add(food);
        }

        return foods;
    }


}
