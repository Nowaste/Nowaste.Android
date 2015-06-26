package com.yacorso.nowaste.dao;

import com.raizlabs.android.dbflow.runtime.TransactionManager;
import com.raizlabs.android.dbflow.runtime.transaction.BaseTransaction;
import com.raizlabs.android.dbflow.runtime.transaction.TransactionListener;
import com.raizlabs.android.dbflow.runtime.transaction.process.InsertModelTransaction;
import com.raizlabs.android.dbflow.runtime.transaction.process.ProcessModelInfo;
import com.raizlabs.android.dbflow.runtime.transaction.process.SaveModelTransaction;
import com.raizlabs.android.dbflow.sql.builder.Condition;
import com.raizlabs.android.dbflow.sql.language.Select;

import com.yacorso.nowaste.events.FridgeCreatedEvent;

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
        TransactionListener resultReceiver = new TransactionListener() {
            @Override
            public void onResultReceived(Object o) {
                /**
                 * When food was created, push then FridgeCreatedEvent
                 * For all listeners
                 */
                EventBus.getDefault().post(new FridgeCreatedEvent());
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

    /**
     * Update fridge in database
     *
     * @param item
     */
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

    /**
     * Delete fridge in database
     *
     * @param item
     */
    @Override
    public void delete(Fridge item) {

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
