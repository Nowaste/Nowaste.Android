package com.yacorso.nowaste.services;

import android.content.Context;

import java.util.List;

/**
 * Created by quentin on 16/06/15.
 */
public abstract class Service<T, U> {

    Context context;

    public Service(Context context){
        this.context = context;
    }

    /**
     *  Insert item
     * @param item
     * @return
     */
    public abstract long create(T item);

    /**
     * Update item
     * @param item
     * @return
     */
    public abstract long update(T item);

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
