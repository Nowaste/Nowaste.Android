package com.yacorso.nowaste.data.dao;

import java.util.ArrayList;
import java.util.List;

/**
 * Interface DAO
 * @param <T>
 * @param <U>
 */
public interface Dao<T, U> {


    /**
     *  Insert item in database
     * @param item
     * @return
     */
    long create(T item);

    /**
     * Update item in database
     * @param item
     * @return
     */
    long update(T item);

    /**
     *  Delete item in database
     * @param item
     */
    void delete(T item);

    /**
     *  Get item from database
     * @param id
     * @return
     */
    T get(U id);

    /**
     * Get all object with Type T from database
     * @return
     */
    List<T> all();
}
