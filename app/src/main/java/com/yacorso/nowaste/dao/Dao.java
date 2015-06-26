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

package com.yacorso.nowaste.dao;

import java.util.List;

/**
 * Abstract class DAO
 * @param <T> Class type
 * @param <U> Id type
 */
public abstract class Dao<T, U> {

     /**
     *  Insert item in database
     * @param item
     * @return
     */
    abstract void create(T item);

    /**
     * Update item in database
     * @param item
     * @return
     */
    abstract void update(T item);

    /**
     *  Delete item in database
     * @param item
     */
    abstract void delete(T item);

    /**
     *  Get item from database
     * @param id
     * @return
     */
    abstract T get(U id);

    /**
     * Get all object with Type T from database
     * @return
     */
    abstract List<T> all();

}
