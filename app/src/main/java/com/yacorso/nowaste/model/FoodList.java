package com.yacorso.nowaste.model;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ForeignKey;
import com.raizlabs.android.dbflow.annotation.ForeignKeyReference;
import com.raizlabs.android.dbflow.annotation.OneToMany;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.sql.builder.Condition;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.raizlabs.android.dbflow.structure.cache.BaseCacheableModel;
import com.raizlabs.android.dbflow.structure.container.ForeignKeyContainer;

import java.util.List;

public abstract class FoodList extends BaseCacheableModel {

    /**
     * Attributes
     */

    @Column
    @PrimaryKey(autoincrement = true)
    protected int id;

    @Column
    protected String name;

    protected List<Food> foodList;


    @Column
    @ForeignKey(
            references = {@ForeignKeyReference(columnName = "user_id",
                    columnType = Long.class,
                    foreignColumnName = "id")},
            saveForeignKeyModel = false
    )
    protected ForeignKeyContainer<User> user;


    /**
     * Functions
     */
    public FoodList () { }

    public long getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }


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
