package com.yacorso.nowaste.events;

import com.yacorso.nowaste.models.Food;

/**
 * Created by fgarnier on 07/07/15.
 */
public class CallUpdateFoodEvent {
    Food food;

    public CallUpdateFoodEvent(Food food) {
        this.food = food;
    }

    public Food getFood() {
        return food;
    }
}
