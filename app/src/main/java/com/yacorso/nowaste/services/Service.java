package com.yacorso.nowaste.services;

import android.content.Context;

import com.yacorso.nowaste.webservice.NowasteApi;

import java.util.List;

import de.greenrobot.event.EventBus;

public abstract class Service<T, U> {

    Context mContext;
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
