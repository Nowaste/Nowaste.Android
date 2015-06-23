package com.yacorso.nowaste.dao;

import java.util.List;

/**
 * Interface DAO
 * @param <T>
 * @param <U>
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
