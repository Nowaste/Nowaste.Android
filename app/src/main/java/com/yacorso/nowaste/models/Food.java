package com.yacorso.nowaste.models;

import android.content.ContentValues;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ForeignKey;
import com.raizlabs.android.dbflow.annotation.ForeignKeyReference;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.cache.BaseCacheableModel;
import com.raizlabs.android.dbflow.structure.container.ForeignKeyContainer;
import com.yacorso.nowaste.data.NowasteDatabase;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

@Table(databaseName = NowasteDatabase.NAME)
public class Food extends BaseCacheableModel implements Parcelable {


    /**
     * Attributes
     */

    @Column
    @Expose
    @PrimaryKey(autoincrement = true)
    protected long id;

    @Column
    @Expose
    protected String name;

    @Column
    @Expose
    @ForeignKey(
            references = {@ForeignKeyReference(
                    columnName = "foodfridge_id",
                    columnType = Long.class,
                    foreignColumnName = "id"
            )},
            saveForeignKeyModel = false
    )
    protected FoodFridge foodFridge;


    @Column
    @Expose
    @ForeignKey(
            references = {@ForeignKeyReference(columnName = "fridge_id",
                    columnType = Long.class,
                    foreignColumnName = "id")},
            saveForeignKeyModel = false
    )
    protected ForeignKeyContainer<Fridge> fridge;

    @Column
    @Expose
    @ForeignKey(
            references = {@ForeignKeyReference(columnName = "customlist_id",
                    columnType = Long.class,
                    foreignColumnName = "id")},
            saveForeignKeyModel = false
    )
    protected ForeignKeyContainer<CustomList> customList;

    @Column
    @Expose
    @ForeignKey(
            references = {@ForeignKeyReference(columnName = "user_id",
                    columnType = Long.class,
                    foreignColumnName = "id")},
            saveForeignKeyModel = false
    )
    protected ForeignKeyContainer<User> user;


    /**
     * Functions
     */

    public Food() {
    }

    public Food(long id, String name) {
        this.id = id;
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

    public boolean hasFoodFridge() {
        boolean hasFoodFridge = false;
        if (this.foodFridge != null) {
            hasFoodFridge = true;
        }

        return hasFoodFridge;
    }

    public ForeignKeyContainer<Fridge> getFridge() {
        return fridge;
    }

    public boolean hasFridge() {
        boolean hasFridge = true;
        if (this.fridge != null) {
            hasFridge = true;
        }

        return hasFridge;
    }

//    public void setFridge(ForeignKeyContainer<Fridge> fridge) {
//        this.fridge = fridge;
//    }


    public void setFridge(Fridge fridge) {
        if(! fridge.isEmpty()){
            this.fridge = new ForeignKeyContainer<>(Fridge.class);
            Map<String, Object> keys = new LinkedHashMap<>();
            keys.put(User$Table.ID, fridge.id);
            this.fridge.setData(keys);
            this.fridge.setModel(fridge);
        }
    }

    public ForeignKeyContainer<CustomList> getCustomList() {
        return customList;
    }

    public void setCustomList(ForeignKeyContainer<CustomList> customList) {
        this.customList = customList;
    }

    public boolean hasCustomList() {
        boolean hasCustomList = false;
        if (this.customList != null) {
            hasCustomList = true;
        }

        return hasCustomList;
    }

    public ForeignKeyContainer<User> getUser() {
        return user;
    }

    public void setUser(ForeignKeyContainer<User> user) {
        this.user = user;
    }

    public boolean isEmpty() {
        boolean isEmpty = false;

        if (name.isEmpty()) {
            isEmpty = true;
        }
        return isEmpty;
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
