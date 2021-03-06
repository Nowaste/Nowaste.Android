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
import com.raizlabs.android.dbflow.runtime.transaction.TransactionListenerAdapter;
import com.raizlabs.android.dbflow.runtime.transaction.process.DeleteModelListTransaction;
import com.raizlabs.android.dbflow.runtime.transaction.process.ProcessModelInfo;
import com.raizlabs.android.dbflow.runtime.transaction.process.ProcessModelTransaction;
import com.raizlabs.android.dbflow.runtime.transaction.process.SaveModelTransaction;
import com.raizlabs.android.dbflow.runtime.transaction.process.UpdateModelListTransaction;
import com.raizlabs.android.dbflow.sql.builder.Condition;
import com.raizlabs.android.dbflow.sql.language.Delete;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.raizlabs.android.dbflow.structure.AsyncModel;
import com.raizlabs.android.dbflow.structure.Model;
import com.yacorso.nowaste.events.UserCreatedEvent;
import com.yacorso.nowaste.events.UserDeletedEvent;
import com.yacorso.nowaste.events.UserUpdatedEvent;
import com.yacorso.nowaste.models.CustomList;
import com.yacorso.nowaste.models.FoodList;
import com.yacorso.nowaste.models.Fridge;
import com.yacorso.nowaste.models.User;
import com.yacorso.nowaste.models.User$Table;
import com.yacorso.nowaste.utils.Utils;

import java.util.List;

import de.greenrobot.event.EventBus;


/**
 * UserDao
 * Relation with database
 */
public class UserDao extends Dao<User, Long> {

    int type;

    /**
     * Insert item in database
     *
     * @param item
     * @return
     */
    public void create(final User item) {
        type = Utils.TYPE_CREATE;
        transact(item);
    }


    /**
     * Update item in database
     *
     * @param item
     * @return
     */
    public void update(User item) {
        type = Utils.TYPE_UPDATE;
        transact(item);
    }

    public void transact(final User user) {
        final AsyncModel.OnModelChangedListener callback = new AsyncModel.OnModelChangedListener() {
            @Override
            public void onModelChanged(Model model) {
                User user = (User) model;

                if (type == Utils.TYPE_CREATE) {
                    EventBus.getDefault().post(new UserCreatedEvent(user));
                } else if (type == Utils.TYPE_UPDATE) {
                    EventBus.getDefault().post(new UserUpdatedEvent(user));
                }
            }
        };

        if (type == Utils.TYPE_CREATE) {
            user.async().withListener(callback).save();
        }
        else if (type == Utils.TYPE_UPDATE){
            final TransactionListenerAdapter resultCustomLists = new TransactionListenerAdapter() {
                @Override
                public void onResultReceived(Object o) {
                    user.async().withListener(callback).update();
                }
            };
            TransactionListenerAdapter resultFridges = new TransactionListenerAdapter() {
                @Override
                public void onResultReceived(Object o) {
                    updateCustomLists(user, resultCustomLists);
                }
            };

            if (user.getFridges() != null) {
                updateFridges(user, resultFridges);
            }
            else {
                updateCustomLists(user, resultCustomLists);
            }

        }
    }

    private void updateFridges(final User user, TransactionListenerAdapter result) {
        if (user.getFridges() != null) {
            ProcessModelInfo processModelInfo = ProcessModelInfo.withModels(user.getFridges()).result(result);
            ProcessModelTransaction transaction = new UpdateModelListTransaction(processModelInfo);
            TransactionManager.getInstance().addTransaction(transaction);
        }

    }

    private void updateCustomLists(User user, TransactionListenerAdapter result) {
        if (user.getCustomLists() != null) {
            ProcessModelInfo processModelInfo = ProcessModelInfo.withModels(user.getCustomLists()).result(result);
            ProcessModelTransaction transaction = new UpdateModelListTransaction(processModelInfo);
            TransactionManager.getInstance().addTransaction(transaction);
        }
    }

    /**
     * Delete item in database
     *
     * @param item
     */
    public void delete(User item) {
        type = Utils.TYPE_DELETE;

        final AsyncModel.OnModelChangedListener callback = new AsyncModel.OnModelChangedListener() {
            @Override
            public void onModelChanged(Model model) {
                EventBus.getDefault().post(new UserDeletedEvent());
            }
        };

        item.async().withListener(callback).delete();
    }

    /**
     * Get user from database
     *
     * @param id
     * @return
     */
    @Override
    public User get(Long id) {
        return new Select()
                .from(User.class)
                .where(Condition.column(User$Table.ID).is(id))
                .querySingle();
    }

    /**
     * Get all users from database
     *
     * @return
     */
    @Override
    public List<User> all() {
        return new Select().from(User.class).queryList();
    }
}
