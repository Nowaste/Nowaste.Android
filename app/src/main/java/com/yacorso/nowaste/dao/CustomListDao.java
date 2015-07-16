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
import com.raizlabs.android.dbflow.runtime.transaction.TransactionListenerAdapter;
import com.raizlabs.android.dbflow.runtime.transaction.process.ProcessModelInfo;
import com.raizlabs.android.dbflow.runtime.transaction.process.ProcessModelTransaction;
import com.raizlabs.android.dbflow.runtime.transaction.process.UpdateModelListTransaction;
import com.raizlabs.android.dbflow.sql.builder.Condition;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.raizlabs.android.dbflow.structure.AsyncModel;
import com.raizlabs.android.dbflow.structure.Model;
import com.yacorso.nowaste.events.CustomListCreatedEvent;
import com.yacorso.nowaste.events.CustomListDeletedEvent;
import com.yacorso.nowaste.events.CustomListUpdatedEvent;
import com.yacorso.nowaste.models.CustomList;
import com.yacorso.nowaste.models.CustomList$Table;
import com.yacorso.nowaste.models.Food;
import com.yacorso.nowaste.models.User;

import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * CustomListDao
 * Relation with database
 */
public class CustomListDao extends Dao<CustomList, Long> {

    int type;

    /**
     * Insert item in database
     *
     * @param item
     * @return
     */
    public void create(final CustomList item) {
        type= TYPE_CREATE;
        transact(item);
    }

    /**
     * Update item in database
     *
     * @param item
     * @return
     */
    public void update(CustomList item) {
        type = TYPE_UPDATE;
        transact(item);
    }

    public void transact(final CustomList item) {
        final AsyncModel.OnModelChangedListener callback = new AsyncModel.OnModelChangedListener() {
            @Override
            public void onModelChanged(Model model) {
                CustomList customList = (CustomList) model;

                User user = customList.getUser();
                if (type == TYPE_CREATE) {
                    user.addCustomList(customList);
                }
                user.async().update();

                if (type == TYPE_CREATE) {
                    EventBus.getDefault().post(new CustomListCreatedEvent(customList));
                } else if (type == TYPE_UPDATE) {
                    EventBus.getDefault().post(new CustomListUpdatedEvent(customList));
                }
            }
        };

        if (type == TYPE_CREATE) {
            item.async().withListener(callback).save();
        }
        else if (type == TYPE_UPDATE){
            TransactionListenerAdapter resultFoods = new TransactionListenerAdapter() {
                @Override
                public void onResultReceived(Object o) {
                    item.async().withListener(callback).update();
                }
            };
            updateFood(item.getFoods(), resultFoods);
        }
    }

    private void updateFood(final List<Food> foods, final TransactionListenerAdapter result) {
        ProcessModelInfo processModelInfo = ProcessModelInfo.withModels(foods).result(result);
        ProcessModelTransaction transaction = new UpdateModelListTransaction(processModelInfo);
        TransactionManager.getInstance().addTransaction(transaction);
    }

    /**
     * Delete item in database
     *
     * @param item
     */
    public void delete(CustomList item) {
        type = TYPE_DELETE;
        User user = item.getUser();
        user.removeCustomList(item);
        user.async().update();

        final AsyncModel.OnModelChangedListener callback = new AsyncModel.OnModelChangedListener() {
            @Override
            public void onModelChanged(Model model) {
                EventBus.getDefault().post(new CustomListDeletedEvent());
            }
        };

        item.async().withListener(callback).delete();
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
