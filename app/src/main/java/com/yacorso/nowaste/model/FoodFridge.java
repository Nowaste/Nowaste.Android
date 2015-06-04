package com.yacorso.nowaste.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ModelContainer;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.cache.BaseCacheableModel;
import com.yacorso.nowaste.data.NowasteDatabase;

import org.apache.http.protocol.HttpDateGenerator;

import java.util.Date;

@ModelContainer
@Table(databaseName = NowasteDatabase.NAME)
public class FoodFridge extends BaseCacheableModel implements Parcelable {


    /**
     * Attributes
     */

    @Column
    @PrimaryKey(autoincrement = true)
    protected int id;

    @Column
    protected Date outOfDate;

    @Column
    protected Date consumedDate;

    @Column
    protected int quantity;

    @Column
    protected Boolean visible;

    @Column
    protected Boolean open;




    /**
     * Functions
     */

    public FoodFridge() {}

    public FoodFridge(int id, Date outOfDate, Date consumedDate, int quantity, Boolean open,
                      Boolean visible) {
        this.id = id;
        this.outOfDate = outOfDate;
        this.consumedDate = consumedDate;
        this.quantity = quantity;
        this.open = open;
        this.visible = visible;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getOutOfDate() {
        return outOfDate;
    }

    public void setOutOfDate(Date outOfDate) {
        this.outOfDate = outOfDate;
    }

    public Date getConsumedDate() {
        return consumedDate;
    }

    public void setConsumedDate(Date consumedDate) {
        this.consumedDate = consumedDate;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Boolean getVisible() {
        return visible;
    }

    public void setVisible(Boolean visible) {
        this.visible = visible;
    }

    public Boolean getOpen() {
        return open;
    }

    public void setOpen(Boolean open) {
        this.open = open;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}
