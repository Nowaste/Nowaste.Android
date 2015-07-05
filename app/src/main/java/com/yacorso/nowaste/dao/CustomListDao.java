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
import com.raizlabs.android.dbflow.runtime.transaction.process.UpdateModelListTransaction;
import com.raizlabs.android.dbflow.sql.builder.Condition;
import com.raizlabs.android.dbflow.sql.language.Delete;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.yacorso.nowaste.events.CustomListCreatedEvent;
import com.yacorso.nowaste.events.CustomListUpdatedEvent;
import com.yacorso.nowaste.events.FridgeCreatedEvent;
import com.yacorso.nowaste.events.FridgeUpdatedEvent;
import com.yacorso.nowaste.models.CustomList;
import com.yacorso.nowaste.models.CustomList$Table;
import com.yacorso.nowaste.models.Fridge;

import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by quentin on 05/07/15.
 */
public class CustomListDao extends Dao<CustomList, Long> {

    @Override
    public void create(CustomList item) {
        insert(item, TYPE_CREATE);
    }

    @Override
    public void update(CustomList item) {
        insert(item, TYPE_UPDATE);
    }

    public void insert(final CustomList item, final int type) {
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
                     * When CustomList was created, push then CustomListCreatedEvent
                     * For all listeners
                     */
                    EventBus.getDefault().post(new CustomListCreatedEvent());
                } else if (type == TYPE_UPDATE) {
                    /*
                     * When CustomList was updated, push then CustomListUpdatedEvent
                     * For all listeners
                     */
                    EventBus.getDefault().post(new CustomListUpdatedEvent());
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
        ProcessModelInfo<CustomList> processModelInfo =
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

    @Override
    public void delete(CustomList item) {
        new Delete()
                .from(CustomList.class)
                .where(Condition.column(CustomList$Table.ID).is(item.getId()))
                .query();
    }

    @Override
    public CustomList get(Long id) {
        return new Select()
                .from(CustomList.class)
                .where(Condition.column(CustomList$Table.ID).is(id))
                .querySingle();
    }

    @Override
    public List<CustomList> all() {
        return new Select().from(CustomList.class).queryList();
    }
}
