package com.yacorso.nowaste.view;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.raizlabs.android.dbflow.runtime.TransactionManager;
import com.raizlabs.android.dbflow.runtime.transaction.SelectListTransaction;
import com.raizlabs.android.dbflow.runtime.transaction.TransactionListenerAdapter;
import com.raizlabs.android.dbflow.runtime.transaction.process.ProcessModelInfo;
import com.raizlabs.android.dbflow.runtime.transaction.process.SaveModelTransaction;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.raizlabs.android.dbflow.structure.container.ForeignKeyContainer;
import com.yacorso.nowaste.R;
import com.yacorso.nowaste.model.Food;
import com.yacorso.nowaste.model.FoodFridge;

import java.util.List;

import static com.raizlabs.android.dbflow.runtime.transaction.process.ProcessModelInfo.withModels;

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
        buttonValidate = (Button) findViewById(R.id.food_button_validate);
        buttonValidate.setOnClickListener(this);
        buttonCheck = (Button) findViewById(R.id.food_button_check);
        buttonCheck.setOnClickListener(this);
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
        foodFridge.setOutOfDate(datePicker.getDate);
        foodFridge.setQuantity(Integer.parseInt(numberPicker.toString()));
        food.setName(nameField.toString());
        TransactionManager.getInstance().saveOnSaveQueue(food);
        //TransactionManager.getInstance().addTransaction(new SaveModelListTransaction<>(ProcessModelInfo.withModels(food)));
    }

    private void checkFood () {
        List<Food> foodList = new Select().from(Food.class).queryList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_food, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
