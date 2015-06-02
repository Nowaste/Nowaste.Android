package com.yacorso.nowaste.model;

import android.content.ContentValues;
import android.os.Parcel;
import android.os.Parcelable;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import com.yacorso.nowaste.data.FoodDatabase;

@Table(databaseName = FoodDatabase.NAME)
public class Food extends BaseModel implements Parcelable {

    @Column
    @PrimaryKey(autoincrement = true)
    private long id;
    @Column
    private String title;
    @Column
    private String date;
    @Column
    private int quantity = 0;

    public Food() {}

    public Food(int id, String title, String date, int quantity) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.quantity = quantity;
    }

    public Food(Parcel in) {
        id = in.readInt();
        title = in.readString();
        date = in.readString();
        quantity = in.readInt();
    }

    public long getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int wishlist) { this.quantity = quantity; }

    public boolean isEmpty () {
        if (this.title.isEmpty()) {
            return true;
        }
        else {
            return false;
        }
    }

    public ContentValues getContentValues () {
        ContentValues cV = new ContentValues();
        cV.put("title", title);
        cV.put("date", date);
        cV.put("quantity", quantity);
        return cV;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeLong(id);
        out.writeString(title);
        out.writeString(date);
        out.writeInt(quantity);
    }

    public static final Parcelable.Creator<Food> CREATOR = new Parcelable.Creator<Food>() {
        @Override
        public Food createFromParcel(Parcel in) { return new Food(in); }
        @Override
        public Food[] newArray(int size) { return new Food[size]; }
    };
}
