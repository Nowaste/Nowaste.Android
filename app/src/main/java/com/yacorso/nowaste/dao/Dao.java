package com.yacorso.nowaste.dao;

import com.squareup.otto.Bus;
import com.yacorso.nowaste.bus.BusProvider;

import java.util.List;

/**
 * Interface DAO
 * @param <T>
 * @param <U>
 */
public abstract class Dao<T, U> {


    protected Bus mBus;

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

    /**
     * Get current bus
     * @return
     */
    protected Bus getBus(){
        if(mBus == null){
            mBus = BusProvider.getInstance();
        }
        return mBus;
    }
}
