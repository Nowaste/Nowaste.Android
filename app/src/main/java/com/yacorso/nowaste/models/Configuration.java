/*
 * Copyright (c) 2015.
 *
 * "THE BEER-WARE LICENSE" (Revision 42):
 * Quentin Bontemps <q.bontemps@gmail>  , Florian Garnier <reventlov@tuta.io>
 * and Marjorie Déboté <marjorie.debote@free.com> wrote this file.
 * As long as you retain this notice you can do whatever you want with this stuff.
 * If we meet some day, and you think this stuff is worth it, you can buy me a beer in return.
 *
 * No Waste team
 */

package com.yacorso.nowaste.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ForeignKey;
import com.raizlabs.android.dbflow.annotation.ForeignKeyReference;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.cache.BaseCacheableModel;
import com.yacorso.nowaste.data.NowasteDatabase;


@Table(databaseName = NowasteDatabase.NAME)
public class Configuration extends BaseCacheableModel implements Parcelable {

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

    @Column
    @Expose
    protected String key;

    @Column
    @Expose
    protected String value;

    @Column
    @Expose
    @ForeignKey(
            references = {@ForeignKeyReference(
                            columnName = "user_id",
                            columnType = Long.class,
                            foreignColumnName = "id"
                        )}
    )
    protected User user;

    /**
     * Functions
     */

    public Configuration() {

    }

    public Configuration(long id, String name, String key, String value) {
        this.id = id;
        this.name = name;
        this.key = key;
        this.value = value;
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

    public String getKey() {
        return key;
    }
    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }
    public void setValue(String value) {
        this.value = value;
    }

    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeLong(this.id);
        dest.writeString(this.name);
        dest.writeString(this.key);
        dest.writeString(this.value);

    }
}
