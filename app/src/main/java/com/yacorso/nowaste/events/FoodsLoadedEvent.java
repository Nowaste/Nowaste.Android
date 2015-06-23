package com.yacorso.nowaste.events;

import com.yacorso.nowaste.models.Food;

import java.util.List;

/**
 * Created by quentin on 22/06/15.
 */
public class FoodsLoadedEvent {


    List<Food> mFoods;

    public FoodsLoadedEvent(List<Food> foods) {
        mFoods = foods;
    }
}
