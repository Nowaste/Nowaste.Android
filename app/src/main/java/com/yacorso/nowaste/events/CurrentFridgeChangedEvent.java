package com.yacorso.nowaste.events;

import com.yacorso.nowaste.models.Fridge;

/**
 * Created by quentin on 22/06/15.
 */
public class CurrentFridgeChangedEvent {

    Fridge mFridge;


    public CurrentFridgeChangedEvent(Fridge fridge) {
        mFridge = fridge;
    }

    public Fridge getFridge() {
        return mFridge;
    }
}
