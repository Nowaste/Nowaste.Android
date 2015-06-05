package com.yacorso.nowaste.model;

import com.raizlabs.android.dbflow.annotation.ModelContainer;
import com.raizlabs.android.dbflow.annotation.Table;

import com.yacorso.nowaste.data.NowasteDatabase;

@ModelContainer
@Table(databaseName = NowasteDatabase.NAME)
public class Fridge extends FoodList {
    public Fridge () { }
}
