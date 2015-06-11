package com.yacorso.nowaste.data.dao;

import android.util.Log;

import com.raizlabs.android.dbflow.runtime.DBTransactionInfo;
import com.raizlabs.android.dbflow.runtime.TransactionManager;
import com.raizlabs.android.dbflow.runtime.transaction.BaseTransaction;
import com.raizlabs.android.dbflow.runtime.transaction.TransactionListener;
import com.raizlabs.android.dbflow.runtime.transaction.process.ProcessModelInfo;
import com.raizlabs.android.dbflow.runtime.transaction.process.SaveModelTransaction;
import com.yacorso.nowaste.model.Food;
import com.yacorso.nowaste.model.FoodFridge;
import com.yacorso.nowaste.model.FoodList;

import java.util.ArrayList;
import java.util.Date;

import static com.yacorso.nowaste.util.Utils.getDateFromDatePicker;

public class FoodDao implements Dao<Food, Integer> {
    @Override
    public long create(final Food item) {

        TransactionListener resultReceiverFoodFridge = new TransactionListener() {
            @Override
            public void onResultReceived(Object o) {

                ArrayList<FoodList> result = (ArrayList<FoodList>) o;

                if(! result.isEmpty()) {
                    o = result.get(0);

                    if (o != null && o.getClass() == FoodFridge.class) {
                        Log.d("CallBackTransaction", "YAY");

                        item.setFoodFridge((FoodFridge) o);
                        TransactionManager.getInstance().saveOnSaveQueue(item);
                    }
                    Log.d("CallBackTransaction", "onResultReceived");
                }

            }

            @Override
            public boolean onReady(BaseTransaction baseTransaction) {
                return true;
            }

            @Override
            public boolean hasResult(BaseTransaction baseTransaction, Object o) {
                Log.d("CallBackTransaction", o.toString());
                return true;
            }
        };




//        TransactionManager.getInstance().addTransaction(new SaveModelTransaction<>(processModelInfo));
        Log.d("CallBackTransaction", "Food");

        if(item.hasFridge())
        {
            Log.d("CallBackTransaction", "hasFridge");

            if(item.hasFoodFridge()) {
                /**
                 * Save Food Fridge
                 */
//                TransactionManager.getInstance().saveOnSaveQueue(item.getFoodFridge());
//                TransactionManager.getInstance().addTransaction(new SaveModelTransaction<>(processModelInfo));
                Log.d("CallBackTransaction", "HasFoodFridge");

                FoodFridge foodFridge = item.getFoodFridge();


                ProcessModelInfo<FoodFridge> processModelInfoFoodFridge =
                        ProcessModelInfo.withModels(foodFridge)
                                        .result(resultReceiverFoodFridge);

                TransactionManager.getInstance().addTransaction(new SaveModelTransaction<>(processModelInfoFoodFridge));


            }else{
                /**
                 * ERROR
                 */
            }
        }

        return 0;
    }

    @Override
    public long update(Food item) {
        return 0;
    }

    @Override
    public void delete(Food item) {

    }

    @Override
    public Food get(Integer id) {
        return null;
    }

    @Override
    public ArrayList<Food> getAll() {
        return null;
    }
}
