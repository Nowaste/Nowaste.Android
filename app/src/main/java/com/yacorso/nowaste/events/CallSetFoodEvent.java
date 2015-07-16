package com.yacorso.nowaste.events;

import com.yacorso.nowaste.models.Food;

public class CallSetFoodEvent {
    Food food;

    public CallSetFoodEvent(Food food) {
        this.food = food;
    }

    public Food getFood() {
        return food;
    }


}
