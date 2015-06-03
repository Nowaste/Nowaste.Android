package com.yacorso.nowaste.model;

import com.raizlabs.android.dbflow.annotation.ModelContainer;
import com.raizlabs.android.dbflow.annotation.Table;
import com.yacorso.nowaste.data.FoodDatabase;

@ModelContainer
@Table(databaseName = FoodDatabase.NAME)
public class CustomList extends FoodList {
    public CustomList () { }
}
