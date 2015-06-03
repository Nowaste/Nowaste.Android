package com.yacorso.nowaste.model;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;

import com.raizlabs.android.dbflow.structure.BaseModel;
import com.raizlabs.android.dbflow.structure.cache.BaseCacheableModel;
import com.yacorso.nowaste.data.FoodDatabase;

@Table(databaseName = FoodDatabase.NAME)
public class User extends BaseCacheableModel {
    @Column
    @PrimaryKey(autoincrement = true)
    protected int id;
    @Column
    protected String name;

    public User () { }

    public long getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}
