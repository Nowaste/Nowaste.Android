package com.yacorso.nowaste.events;

import com.yacorso.nowaste.models.Fridge;

import java.util.List;

/**
 * Created by quentin on 21/06/15.
 */
public class FridgesLoadedEvent {

    List<Fridge> mFridges;


    public FridgesLoadedEvent(List<Fridge> fridges) {
        mFridges = fridges;
    }

    public List<Fridge> getFridges(){
        return mFridges;
    }
}
