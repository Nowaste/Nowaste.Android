/*
 * Copyright (c) 2015.
 *
 * "THE BEER-WARE LICENSE" (Revision 42):
 * <q.bontemps@gmail> , <reventlov@tuta.io> and <marjorie.debote@free.com> wrote this file.
 *  As long as you retain this notice you can do whatever you want with this stuff.
 *  If we meet some day, and you think this stuff is worth it, you can buy me a beer in return.
 *
 * No Waste team
 *
 */

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
