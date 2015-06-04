package com.yacorso.nowaste.model;

import android.content.ContentValues;
import android.os.Parcel;
import android.os.Parcelable;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ForeignKey;
import com.raizlabs.android.dbflow.annotation.ForeignKeyReference;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;

import com.raizlabs.android.dbflow.structure.cache.BaseCacheableModel;
import com.raizlabs.android.dbflow.structure.container.ForeignKeyContainer;
import com.yacorso.nowaste.data.NowasteDatabase;

@Table(databaseName = NowasteDatabase.NAME)
public class Food extends BaseCacheableModel implements Parcelable {


    /**
     * Attributes
     */

    @Column
    @PrimaryKey(autoincrement = true)
    protected int id;

    @Column
    protected String name;

    @Column
    @ForeignKey(
            references = {@ForeignKeyReference(
                    columnName = "foodfridge_id",
                    columnType = Long.class,
                    foreignColumnName = "id"
            )}
    )
    protected ForeignKeyContainer<FoodFridge> foodFridge;


    @Column
    @ForeignKey(
        references = {@ForeignKeyReference(columnName = "fridge_id",
                columnType = Long.class,
                foreignColumnName = "id")},
        saveForeignKeyModel = false
    )
    protected ForeignKeyContainer<Fridge> fridge;

    @Column
    @ForeignKey(
            references = {@ForeignKeyReference(columnName = "customlist_id",
                    columnType = Long.class,
                    foreignColumnName = "id")},
            saveForeignKeyModel = false
    )
    protected ForeignKeyContainer<CustomList> customList;

    @Column
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

    public Food() {}

    public Food(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Food(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
    }

    public long getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ForeignKeyContainer<FoodFridge> getFoodFridge() {
        return foodFridge;
    }

    public void setFoodFridge(ForeignKeyContainer<FoodFridge> foodFridge) {
        this.foodFridge = foodFridge;
    }

    public ForeignKeyContainer<Fridge> getFridge() {
        return fridge;
    }

    public void setFridge(ForeignKeyContainer<Fridge> fridge) {
        this.fridge = fridge;
    }

    public ForeignKeyContainer<CustomList> getCustomList() {
        return customList;
    }

    public void setCustomList(ForeignKeyContainer<CustomList> customList) {
        this.customList = customList;
    }

    public ForeignKeyContainer<User> getUser() {
        return user;
    }

    public void setUser(ForeignKeyContainer<User> user) {
        this.user = user;
    }

    public boolean isEmpty () {
        if (this.name.isEmpty()) {
            return true;
        }
        else {
            return false;
        }
    }

    public ContentValues getContentValues () {
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

    public static final Parcelable.Creator<Food> CREATOR = new Parcelable.Creator<Food>() {
        @Override
        public Food createFromParcel(Parcel in) { return new Food(in); }
        @Override
        public Food[] newArray(int size) { return new Food[size]; }
    };

    @Override
    public String toString() {
        return "Food [id =" + this.id + ", name =" + this.name + "]";
    }
}
