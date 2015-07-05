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

package com.yacorso.nowaste.providers;

import com.yacorso.nowaste.data.NowasteApi;

import java.util.List;

/**
 * Abstract class Service
 * @param <T> Object class
 * @param <U> Id class
 */
public abstract class Provider<T, U> {

    NowasteApi mApi;

    public Provider(){
        mApi = NowasteApi.ApiInstance.getInstance();
    }

    public Provider(NowasteApi api)
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
