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

package com.yacorso.nowaste.dao;

import com.raizlabs.android.dbflow.runtime.transaction.TransactionListener;

import java.util.List;

/**
 * Abstract class DAO
 *
 * @param <T> Class type
 * @param <U> Id type
 */
public abstract class Dao<T, U> {

    static final int TYPE_UPDATE = 1;
    static final int TYPE_CREATE = 2;
    static final int TYPE_DELETE = 3;
    static final int TYPE_SELECT = 4;

    /**
     * Insert item in database
     *
     * @param item
     * @return
     */
    public abstract void create(T item);

    /**
     * Update item in database
     *
     * @param item
     * @return
     */
    public abstract void update(T item);

    /**
     * Delete item in database
     *
     * @param item
     */
    public abstract void delete(T item);

    /**
     * Get item from database
     *
     * @param id
     * @return
     */
    public abstract T get(U id);

    /**
     * Get all object with Type T from database
     *
     * @return
     */
    public abstract List<T> all();

}