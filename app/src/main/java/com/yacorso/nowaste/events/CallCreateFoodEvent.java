package com.yacorso.nowaste.events;

import com.yacorso.nowaste.models.Food;

public class CallCreateFoodEvent {
    Food food;

    public CallCreateFoodEvent(Food food) {
        this.food = food;
    }

    public Food getFood() {
        return food;
    }


}
