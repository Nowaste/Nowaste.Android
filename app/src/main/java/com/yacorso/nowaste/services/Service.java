package com.yacorso.nowaste.services;

import android.content.Context;

import com.squareup.otto.Bus;
import com.yacorso.nowaste.webservice.NowasteApi;

import java.util.List;

/**
 * Created by quentin on 16/06/15.
 */
public abstract class Service<T, U> {

    Context mContext;
    NowasteApi mApi;
    Bus mBus;

    public Service(Bus bus){
        mBus= bus;
        mApi = NowasteApi.ApiInstance.getInstance();
    }

    public Service(NowasteApi api, Bus bus)
    {
        mApi = api;
        mBus = bus;
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
