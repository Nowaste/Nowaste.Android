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

import com.raizlabs.android.dbflow.runtime.TransactionManager;
import com.raizlabs.android.dbflow.runtime.transaction.BaseTransaction;
import com.raizlabs.android.dbflow.runtime.transaction.TransactionListener;
import com.raizlabs.android.dbflow.runtime.transaction.process.InsertModelTransaction;
import com.raizlabs.android.dbflow.runtime.transaction.process.ProcessModelInfo;
import com.raizlabs.android.dbflow.runtime.transaction.process.SaveModelTransaction;
import com.raizlabs.android.dbflow.runtime.transaction.process.UpdateModelListTransaction;
import com.raizlabs.android.dbflow.sql.builder.Condition;
import com.raizlabs.android.dbflow.sql.language.Delete;
import com.raizlabs.android.dbflow.sql.language.Select;

import com.yacorso.nowaste.events.FridgeCreatedEvent;

import com.yacorso.nowaste.events.FridgeUpdatedEvent;
import com.yacorso.nowaste.models.Fridge;
import com.yacorso.nowaste.models.Fridge$Table;

import java.util.List;

import de.greenrobot.event.EventBus;


/**
 * FridgeDao
 * Relation with database
 */
public class FridgeDao extends Dao<Fridge, Long> {

    /**
     * Create a fridge in database
     *
     * @param item
     */
    @Override
    public void create(Fridge item) {
        insert(item, TYPE_CREATE);
    }

    /**
     * Update fridge in database
     *
     * @param item
     */
    @Override
    public void update(final Fridge item) {
        insert(item, TYPE_UPDATE);
    }

    public void insert(final Fridge item, final int type) {
        TransactionListener resultReceiver = new TransactionListener() {
            @Override
            public void onResultReceived(Object o) {
                /*
                 * Insert or update foods items
                 */
                FoodDao foodDao = new FoodDao();
                foodDao.insert(item.getFoods());

                if (type == TYPE_CREATE) {

                    /*
                     * When fridge was created, push then FridgeCreatedEvent
                     * For all listeners
                     */
                    EventBus.getDefault().post(new FridgeCreatedEvent());
                } else if (type == TYPE_UPDATE) {
                    /*
                     * When fridge was updated, push then FridgeUpdatedEvent
                     * For all listeners
                     */
                    EventBus.getDefault().post(new FridgeUpdatedEvent());
                }
            }

            @Override
            public boolean onReady(BaseTransaction baseTransaction) {
                return baseTransaction.onReady();
            }

            @Override
            public boolean hasResult(BaseTransaction baseTransaction, Object o) {
                return true;
            }
        };


        /*
         * Set DBFlow Transaction
         */
        ProcessModelInfo<Fridge> processModelInfo =
                ProcessModelInfo.withModels(item)
                        .result(resultReceiver);

        if (type == TYPE_CREATE) {
            TransactionManager.getInstance()
                    .addTransaction(new InsertModelTransaction<>(processModelInfo));
        } else if (type == TYPE_UPDATE) {
            TransactionManager.getInstance()
                    .addTransaction(new UpdateModelListTransaction<>(processModelInfo));
        }
    }

    /**
     * Delete fridge in database
     *
     * @param item
     */
    @Override
    public void delete(Fridge item) {
        new Delete()
                .from(Fridge.class)
                .where(Condition.column(Fridge$Table.ID).is(item.getId()))
                .query();
    }

    /**
     * Get a fridge from database
     *
     * @param id
     * @return
     */
    @Override
    public Fridge get(Long id) {

        Fridge f = new Select().from(Fridge.class)
                .where(Condition.column(Fridge$Table.ID).is(id)).querySingle();


        return f;

    }

    /**
     * Get all fridges from database
     *
     * @return
     */
    @Override
    public List<Fridge> all() {
        return new Select().all().from(Fridge.class).queryList();
    }
}
