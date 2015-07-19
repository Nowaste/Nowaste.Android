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

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ModelContainer;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.yacorso.nowaste.data.NowasteDatabase;

import java.util.Date;

import static com.yacorso.nowaste.utils.DateUtils.addDaysToDate;
import static com.yacorso.nowaste.utils.DateUtils.removeDaysToDate;

@ModelContainer
@Table(databaseName = NowasteDatabase.NAME)
public class FoodFridge extends Model implements Parcelable {


    /**
     * Attributes
     */

    @Column
    @Expose
    @PrimaryKey(autoincrement = true)
    long id;

    @Column
    @Expose
    @SerializedName("out_of_date")
    Date outOfDate;

    @Column
    @Expose
    Date consumedDate;

    @Column
    @Expose
    int quantity;

    @Column
    @Expose
    Boolean visible;

    @Column
    @Expose
    Boolean open;


    /**
     * Functions
     */

    public FoodFridge() {
        this.id = 0;
        this.outOfDate = null;
        this.consumedDate = null;
        this.quantity = 1;
        this.open = false;
        this.visible = false;
    }

    public FoodFridge(long id, Date outOfDate, Date consumedDate, int quantity, Boolean open,
                      Boolean visible) {
        this.id = id;
        this.outOfDate = outOfDate;
        this.consumedDate = consumedDate;
        this.quantity = quantity;
        this.open = open;
        this.visible = visible;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
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

    public Boolean isOpen() {
        if (open == null) {
            open = false;
        }
        return open;
    }

    public void setOpen(Boolean open) {
        this.open = open;
    }

    public void toggleOpen(){
        setOpen(!isOpen());
        changeOutOfDate();
    }

    public boolean isEmpty() {
        boolean isEmpty = true;

        if (outOfDate != null) {
            isEmpty = false;
        }

        return isEmpty;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }

    @Override
    public String toString() {
        return "FoodFridge{" +
                "id=" + id +
                ", outOfDate=" + outOfDate +
                ", consumedDate=" + consumedDate +
                ", quantity=" + quantity +
                ", visible=" + visible +
                ", open=" + open +
                '}';
    }

    private void changeOutOfDate () {
        //TODO: Fetch the number of days from preferences
        Date newOutOfDate;
        int days = 4;
        if (open) {
            newOutOfDate = removeDaysToDate(outOfDate, days);
        }
        else {
            newOutOfDate = addDaysToDate(outOfDate, days);
        }

        setOutOfDate(newOutOfDate);
    }
}
