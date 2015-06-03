package com.yacorso.nowaste.model;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ModelContainer;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;

import com.raizlabs.android.dbflow.structure.BaseModel;
import com.yacorso.nowaste.data.FoodDatabase;

@ModelContainer
@Table(databaseName = FoodDatabase.NAME)
public class Fridge extends FoodList {
    public Fridge () { }
}
