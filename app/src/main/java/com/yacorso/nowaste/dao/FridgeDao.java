package com.yacorso.nowaste.dao;

import com.raizlabs.android.dbflow.runtime.TransactionManager;
import com.raizlabs.android.dbflow.runtime.transaction.BaseTransaction;
import com.raizlabs.android.dbflow.runtime.transaction.TransactionListener;
import com.raizlabs.android.dbflow.runtime.transaction.process.InsertModelTransaction;
import com.raizlabs.android.dbflow.runtime.transaction.process.ProcessModelInfo;
import com.raizlabs.android.dbflow.runtime.transaction.process.SaveModelTransaction;
import com.raizlabs.android.dbflow.runtime.transaction.process.UpdateModelListTransaction;
import com.raizlabs.android.dbflow.sql.builder.Condition;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.yacorso.nowaste.NowasteApplication;
import com.yacorso.nowaste.events.CurrentFridgeChangedEvent;
import com.yacorso.nowaste.events.FoodCreatedEvent;
import com.yacorso.nowaste.models.Food;
import com.yacorso.nowaste.models.Fridge;
import com.yacorso.nowaste.models.Fridge$Table;
import com.yacorso.nowaste.models.User;
import com.yacorso.nowaste.webservice.NowasteApi;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by quentin on 22/06/15.
 */
public class FridgeDao extends Dao<Fridge, Long> {
    @Override
    public void create(Fridge item) {
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
        ProcessModelInfo<Fridge> processModelInfo =
                ProcessModelInfo.withModels(item)
                        .result(resultReceiver);
        TransactionManager.getInstance().addTransaction(
                new SaveModelTransaction<>(processModelInfo));
    }

    @Override
    public void update(final Fridge item) {

        TransactionListener resultReceiver = new TransactionListener() {
            @Override
            public void onResultReceived(Object o) {
                FoodDao foodDao = new FoodDao();
                foodDao.insert(item.getFoods());
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
        ProcessModelInfo<Fridge> processModelInfo =
                ProcessModelInfo.withModels(item)
                        .result(resultReceiver);

        TransactionManager.getInstance()
                .addTransaction(new InsertModelTransaction<>(processModelInfo));

    }

    @Override
    public void delete(Fridge item) {

    }

    @Override
    public Fridge get(Long id) {

        Fridge f = new Select().from(Fridge.class)
                .where(Condition.column(Fridge$Table.ID).is(id)).querySingle();


        return f;

    }

    @Override
    public List<Fridge> all() {
        return new Select().all().from(Fridge.class).queryList();
    }
}
