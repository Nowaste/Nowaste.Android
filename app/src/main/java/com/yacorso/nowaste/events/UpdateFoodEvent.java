package com.yacorso.nowaste.events;

import com.yacorso.nowaste.models.Food;

/**
 * Created by fgarnier on 07/07/15.
 */
public class UpdateFoodEvent {
    Food food;

    public UpdateFoodEvent (Food food) {
        this.food = food;
    }

    public Food getFood() {
        return food;
    }
}
