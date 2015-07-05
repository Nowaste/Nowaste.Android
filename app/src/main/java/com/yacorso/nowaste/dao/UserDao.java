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
import com.raizlabs.android.dbflow.runtime.transaction.process.ProcessModelInfo;
import com.raizlabs.android.dbflow.runtime.transaction.process.SaveModelTransaction;
import com.raizlabs.android.dbflow.runtime.transaction.process.UpdateModelListTransaction;
import com.raizlabs.android.dbflow.sql.builder.Condition;
import com.raizlabs.android.dbflow.sql.language.Delete;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.yacorso.nowaste.events.UserCreatedEvent;
import com.yacorso.nowaste.events.UserUpdatedEvent;
import com.yacorso.nowaste.models.User;
import com.yacorso.nowaste.models.User$Table;

import java.util.List;

import de.greenrobot.event.EventBus;


/**
 * UserDao
 * Relation with database
 */
public class UserDao extends Dao<User, Long> {

    /**
     * Create user in database
     *
     * @param item
     */
    @Override
    public void create(User item) {
        insert(item, TYPE_CREATE);
    }

    /**
     * Update user in database
     *
     * @param item
     */
    @Override
    public void update(User item) {
        insert(item, TYPE_UPDATE);
    }

    public void insert(User item, final int type) {

        TransactionListener resultReceiver = new TransactionListener() {
            @Override
            public void onResultReceived(Object o) {
                if (type == TYPE_CREATE) {
                    EventBus.getDefault().post(new UserCreatedEvent());
                } else if (type == TYPE_UPDATE) {
                    EventBus.getDefault().post(new UserUpdatedEvent());
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


        /**
         * Set DBFlow Transaction
         */
        ProcessModelInfo<User> processModelInfo =
                ProcessModelInfo.withModels(item)
                        .result(resultReceiver);
        if (type == TYPE_CREATE) {
            TransactionManager.getInstance().addTransaction(
                    new SaveModelTransaction<>(processModelInfo));
        } else if (type == TYPE_UPDATE) {
            TransactionManager.getInstance().addTransaction(
                    new UpdateModelListTransaction<>(processModelInfo));
        }

    }

    /**
     * Delete user in database
     *
     * @param item
     */
    @Override
    public void delete(User item) {
        new Delete()
                .from(User.class)
                .where(Condition.column(User$Table.ID).is(item.getId()))
                .query();
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

        List<User> users = new Select().from(User.class).queryList();

        return users;

    }
}
