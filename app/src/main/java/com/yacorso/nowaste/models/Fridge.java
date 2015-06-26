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

import com.raizlabs.android.dbflow.annotation.ModelContainer;
import com.raizlabs.android.dbflow.annotation.OneToMany;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.sql.builder.Condition;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.yacorso.nowaste.data.NowasteDatabase;

import java.util.List;

/**
 * Created by quentin on 21/06/15.
 */

@ModelContainer
@Table(databaseName = NowasteDatabase.NAME)
public class Fridge extends FoodList {

    public Fridge(){}

    @OneToMany(methods = {OneToMany.Method.ALL})
    public List<Food> getFoods() {
        if(foods == null) {
            foods = new Select()
                    .from(Food.class)
                    .where(Condition.column(Food$Table.FRIDGE_FRIDGE_ID).is(id))
                    .queryList();
        }
        return foods;
    }
}
