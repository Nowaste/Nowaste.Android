/*
 * Copyright (c) 2015.
 *
 * "THE BEER-WARE LICENSE" (Revision 42):
 * <q.bontemps@gmail> , <reventlov@tuta.io> and <marjorie.debote@free.com> wrote this file.
 *  As long as you retain this notice you can do whatever you want with this stuff.
 *  If we meet some day, and you think this stuff is worth it, you can buy me a beer in return.
 *
 * No Waste team
 *
 */

package com.yacorso.nowaste.view.old;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;

import com.raizlabs.android.dbflow.sql.language.Select;
import com.yacorso.nowaste.R;
import com.yacorso.nowaste.dao.FoodDao;
import com.yacorso.nowaste.models.Food;
import com.yacorso.nowaste.models.FoodFridge;

import java.util.List;

import static com.raizlabs.android.dbflow.runtime.transaction.process.ProcessModelInfo.withModels;
import static com.yacorso.nowaste.utils.Utils.getDateFromDatePicker;

public class AddFoodActivity extends Activity implements View.OnClickListener {

    EditText nameField;
    DatePicker datePicker;
    NumberPicker numberPicker;
    Button buttonValidate;
    Button buttonCheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food);
        nameField = (EditText) findViewById(R.id.food_name);
        datePicker = (DatePicker) findViewById(R.id.food_date);
        numberPicker = (NumberPicker) findViewById(R.id.food_quantity);
        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(20);
        numberPicker.setWrapSelectorWheel(false);
        numberPicker.setValue(1);
        buttonValidate = (Button) findViewById(R.id.food_button_validate);
        buttonValidate.setOnClickListener(this);
        buttonCheck = (Button) findViewById(R.id.food_button_check);
        buttonCheck.setOnClickListener(this);

        this.registerReceivers();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.food_button_validate:
                createFood();
                break;
            case R.id.food_button_check:
                checkFood();
        }
    }

    private void createFood () {
        Food food = new Food();
        FoodFridge foodFridge = food.getFoodFridge();
        foodFridge.setOutOfDate(getDateFromDatePicker(datePicker));
        foodFridge.setQuantity(numberPicker.getValue());
        food.setName(nameField.getText().toString());

        FoodDao dao  = new FoodDao();
        dao.create(food);

        Log.e("toto", "tt");

//        TransactionManager.getInstance().saveOnSaveQueue(food);
//        TransactionManager.getInstance().addTransaction(new SaveModelListTransaction<>(ProcessModelInfo.withModels(food)));
    }

    private void checkFood () {
        List<Food> foodList = new Select().from(Food.class).queryList();
        foodList.toString();
    }


    private BroadcastReceiver mConnReveiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            boolean noConnectivity = intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false);
            String reason = intent.getStringExtra(ConnectivityManager.EXTRA_REASON);
            boolean isFailover = intent.getBooleanExtra(ConnectivityManager.EXTRA_IS_FAILOVER, false);

            NetworkInfo currentNetworkInfo = (NetworkInfo) intent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
            NetworkInfo otherNetworkInfo = (NetworkInfo) intent.getParcelableExtra(ConnectivityManager.EXTRA_OTHER_NETWORK_INFO);

            Log.d("BroadcastReceiver", "yep");

        }
    };

    private void registerReceivers() {
        registerReceiver(this.mConnReveiver,
                new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }
}
