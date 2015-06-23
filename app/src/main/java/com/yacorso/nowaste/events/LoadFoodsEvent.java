package com.yacorso.nowaste.events;

import com.yacorso.nowaste.models.Food;
import com.yacorso.nowaste.models.FoodList;

/**
 * Created by quentin on 22/06/15.
 */
public class LoadFoodsEvent {

    protected FoodList mFoodList;

    public LoadFoodsEvent(FoodList foodList) {
        mFoodList = foodList;
    }

    public FoodList getFoodList() {
        return mFoodList;
    }
}
