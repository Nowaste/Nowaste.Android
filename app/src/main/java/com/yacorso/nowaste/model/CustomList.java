package com.yacorso.nowaste.model;

import com.raizlabs.android.dbflow.annotation.ModelContainer;
import com.raizlabs.android.dbflow.annotation.OneToMany;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.sql.builder.Condition;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.yacorso.nowaste.data.NowasteDatabase;

import java.util.List;

@ModelContainer
@Table(databaseName = NowasteDatabase.NAME)
public class CustomList extends FoodList {
    public CustomList () { }

    @OneToMany(methods = {OneToMany.Method.ALL})
    public List<Food> getFoodList() {
        if(foodList == null) {
            foodList = new Select()
                    .from(Food.class)
                    .where(Condition.column(Food$Table.CUSTOMLIST_CUSTOMLIST_ID).is(id))
                    .queryList();
        }
        return foodList;
    }
}
