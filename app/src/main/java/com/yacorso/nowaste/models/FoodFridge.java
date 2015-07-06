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
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ModelContainer;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.cache.BaseCacheableModel;
import com.yacorso.nowaste.data.NowasteDatabase;

import java.util.Date;

@ModelContainer
@Table(databaseName = NowasteDatabase.NAME)
public class FoodFridge extends BaseCacheableModel implements Parcelable {


    /**
     * Attributes
     */

    @Column
    @Expose
    @PrimaryKey(autoincrement = true)
    protected long id;

    @Column
    @Expose
    protected Date outOfDate;

    @Column
    @Expose
    protected Date consumedDate;

    @Column
    @Expose
    protected int quantity;

    @Column
    @Expose
    protected Boolean visible;

    @Column
    @Expose
    protected Boolean open;


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
        return open;
    }

    public void setOpen(Boolean open) {
        this.open = open;
    }

    public void toggleOpen(){
        if (open == null) {
            open = false;
        }
        setOpen(!open);
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
}
