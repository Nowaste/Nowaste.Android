/*
 * Copyright (c) 2015.
 *
 * "THE BEER-WARE LICENSE" (Revision 42):
 * Quentin Bontemps <q.bontemps@gmail>  , Florian Garnier <reventlov@tuta.io>
 * and Marjorie Déboté <marjorie.debote@free.com> wrote this file.
 * As long as you retain this notice you can do whatever you want with this stuff.
 * If we meet some day, and you think this stuff is worth it, you can buy me a beer in return.
 *
 * NoWaste team
 */

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
