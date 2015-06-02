package model;

import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.sql.builder.Condition;
import com.raizlabs.android.dbflow.sql.language.Select;

import java.util.List;

import data.FoodDatabase;

public class FoodList {
    private List<Food> foodList;

    public List<Food> getMyAnts() {
        if(foodList == null) {
            foodList = new Select()
                    .from(Food.class)
                    //TODO: Make Food$Table work
                    //.where(Condition.column(Food$Table).is(id))
                    .queryList();
        }
        return foodList;
    }
}
