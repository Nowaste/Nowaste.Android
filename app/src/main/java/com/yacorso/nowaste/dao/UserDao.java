/*
 * Copyright (c) 2015.
 *
 * "THE BEER-WARE LICENSE" (Revision 42):
 * Quentin Bontemps <q.bontemps@gmail>  , Florian Garnier <reventlov@tuta.io>
 * and Marjorie Déboté <marjorie.debote@free.com> wrote this file.
 * As long as you retain this notice you can do whatever you want with this stuff.
 * If we meet some day, and you think this stuff is worth it, you can buy me a beer in return.
 *
 * No Waste team
 */

package com.yacorso.nowaste.dao;

import com.raizlabs.android.dbflow.runtime.TransactionManager;
import com.raizlabs.android.dbflow.runtime.transaction.BaseTransaction;
import com.raizlabs.android.dbflow.runtime.transaction.TransactionListener;
import com.raizlabs.android.dbflow.runtime.transaction.process.ProcessModelInfo;
import com.raizlabs.android.dbflow.runtime.transaction.process.SaveModelTransaction;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.yacorso.nowaste.models.User;

import java.util.List;


/**
 * UserDao
 * Relation with database
 */
public class UserDao extends Dao<User, Long>{

    /**
     *  Create user in database
     * @param item
     */
    @Override
    public void create(User item) {

        TransactionListener resultReceiver = new TransactionListener() {
            @Override
            public void onResultReceived(Object o) {

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
        TransactionManager.getInstance().addTransaction(
                new SaveModelTransaction<>(processModelInfo));

    }

    /**
     *  Update user in database
     * @param item
     */
    @Override
    public void update(User item) {

    }

    /**
     *  Delete user in database
     * @param item
     */
    @Override
    public void delete(User item) {

    }

    /**
     * Get user from database
     *
     * @param id
     * @return
     */
    @Override
    public User get(Long id) {
        return null;
    }

    /**
     * Get all users from database
     * @return
     */
    @Override
    public List<User> all() {

        List<User> users = new Select().from(User.class).queryList();

        return users;

    }
}
