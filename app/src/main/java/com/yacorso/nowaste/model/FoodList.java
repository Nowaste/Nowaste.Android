package com.yacorso.nowaste.model;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.OneToMany;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.sql.builder.Condition;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.raizlabs.android.dbflow.structure.cache.BaseCacheableModel;
import com.yacorso.nowaste.data.FoodDatabase;

import java.util.List;

public abstract class FoodList extends BaseCacheableModel {

    @Column
    @PrimaryKey(autoincrement = true)
    protected int id;
    @Column
    protected String name;
    protected List<Food> foodList;

    public FoodList () { }

    public long getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }


    @OneToMany(methods = {OneToMany.Method.ALL})
    public List<Food> getFoodList() {
        if(foodList == null) {
            foodList = new Select()
                    .from(Food.class)
                    .where(Condition.column(Food$Table.FOODLIST_FOODLIST_ID).is(id))
                    .queryList();
        }
        return foodList;
    }

    @Override
    public String toString()
    {
        StringBuilder stringBuilder = new StringBuilder();
        for (Food food : foodList) {
            stringBuilder.append(food.toString());
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }
}
