package com.yacorso.nowaste.dao;

import android.util.Log;

import com.raizlabs.android.dbflow.runtime.TransactionManager;
import com.raizlabs.android.dbflow.runtime.transaction.BaseTransaction;
import com.raizlabs.android.dbflow.runtime.transaction.TransactionListener;
import com.raizlabs.android.dbflow.runtime.transaction.process.ProcessModelInfo;
import com.raizlabs.android.dbflow.runtime.transaction.process.SaveModelTransaction;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.yacorso.nowaste.models.User;

import java.util.List;

/**
 * Created by quentin on 22/06/15.
 */
public class UserDao extends Dao<User, Long>{
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

    @Override
    public void update(User item) {

    }

    @Override
    public void delete(User item) {

    }

    @Override
    public User get(Long id) {
        return null;
    }

    @Override
    public List<User> all() {

        List<User> users = new Select().from(User.class).queryList();

        return users;

    }
}
