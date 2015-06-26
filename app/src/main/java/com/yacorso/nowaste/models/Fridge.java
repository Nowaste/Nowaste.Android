package com.yacorso.nowaste.models;

import com.raizlabs.android.dbflow.annotation.ModelContainer;
import com.raizlabs.android.dbflow.annotation.OneToMany;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.sql.builder.Condition;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.yacorso.nowaste.data.NowasteDatabase;
import com.yacorso.nowaste.models.Food$Table;

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
