/*
 * Copyright (c) 2015.
 *
 * "THE BEER-WARE LICENSE" (Revision 42):
 * Quentin Bontemps <q.bontemps@gmail>  , Florian Garnier <reventlov@tuta.io>
 * and Marjorie Déboté <marjorie.debote@free.com> wrote this file.
 * As long as you retain this notice you can do whatever you want with this stuff.
 * If we meet some day, and you think this stuff is worth it, you can buy me a beer in return.
 *
 * No Waste team
 */

package com.yacorso.nowaste.services;

import android.content.Context;

import com.yacorso.nowaste.data.NowasteApi;

import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Abstract class Service
 * @param <T> Object class
 * @param <U> Id class
 */
public abstract class Service<T, U> {

    NowasteApi mApi;

    public Service(){
        mApi = NowasteApi.ApiInstance.getInstance();
    }

    public Service(NowasteApi api)
    {
        mApi = api;
    }

    /**
     *  Insert item
     * @param item
     * @return
     */
    public abstract void create(T item);

    /**
     * Update item
     * @param item
     * @return
     */
    public abstract void update(T item);

    /**
     *  Delete item
     * @param item
     */
    public abstract void delete(T item);

    /**
     *  Get item
     * @param id
     * @return
     */
    public abstract T get(U id);

    /**
     * Get all object with Type T
     * @return
     */
    public abstract List<T> all();

}
