package com.yacorso.nowaste.views.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.yacorso.nowaste.R;
import com.yacorso.nowaste.events.FoodCreatedEvent;
import com.yacorso.nowaste.events.FoodUpdatedEvent;
import com.yacorso.nowaste.models.Food;
import com.yacorso.nowaste.models.FoodFridge;
import com.yacorso.nowaste.models.Fridge;
import com.yacorso.nowaste.providers.FoodProvider;
import com.yacorso.nowaste.providers.FridgeProvider;
import com.yacorso.nowaste.utils.LogUtil;
import com.yacorso.nowaste.utils.DateUtils;

import java.security.SecureRandom;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

import static com.yacorso.nowaste.utils.DateUtils.resetDatePicker;
import static com.yacorso.nowaste.utils.DateUtils.setDatePicker;

public class AddFoodFragment extends BaseFragment {

    EditText nameField;
    DatePicker datePicker;
    NumberPicker numberPicker;

    Fridge mCurrentFridge;
    FoodProvider mFoodProvider;
    FridgeProvider mFridgeProvider;

    public static AddFoodFragment newInstance() { return new AddFoodFragment(); }
    public static AddFoodFragment newInstance(Food food) {
        AddFoodFragment addFoodFragment = new AddFoodFragment();

        Bundle args = new Bundle();
        args.putParcelable("food", food);
        addFoodFragment.setArguments(args);

        return addFoodFragment;
    }

    public AddFoodFragment() { }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mFridgeProvider = new FridgeProvider();
        mFoodProvider = new FoodProvider();
        mCurrentFridge = mFridgeProvider.getCurrentFridge();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return null;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = getActivity().getLayoutInflater().inflate(getLayout(), null);
        ButterKnife.inject(this, view);
        nameField = ButterKnife.findById(view, R.id.name_edit_text);

        datePicker = ButterKnife.findById(view, R.id.out_of_date_picker);
        numberPicker = ButterKnife.findById(view, R.id.quantity_number_picker);
        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(100);
        builder.setView(view);
        builder.setTitle(getTitle());
        builder.setPositiveButton(R.string.add_one_element, null);
        builder.setNeutralButton(R.string.add_another_element, null);
        builder.setNegativeButton(R.string.cancel, null);

        Bundle arguments = getArguments();
        if (arguments != null && arguments.containsKey("food")) {
            addFoodInformationsToDialog(builder);
        }

        AlertDialog dialog = setButtonsListener(builder, arguments);

        nameField.requestFocus();
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        nameField.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                return false;
            }
        });

        return dialog;
    }

    private void addFoodInformationsToDialog (AlertDialog.Builder builder) {
        Food food = getArguments().getParcelable("food");
        nameField.setText(food.getName());
        setDatePicker(datePicker, food.getFoodFridge().getOutOfDate());
        numberPicker.setValue(food.getFoodFridge().getQuantity());

        builder.setPositiveButton(R.string.validate, null);
        builder.setNeutralButton(null, null);
        builder.setNegativeButton(R.string.cancel, null);
    }

    private AlertDialog setButtonsListener (AlertDialog.Builder builder, Bundle arguments) {
        final AlertDialog dialog = builder.create();
        if (arguments != null && arguments.containsKey("food")) {
            final Food food = getArguments().getParcelable("food");
            dialog.setOnShowListener(new DialogInterface.OnShowListener() {

                @Override
                public void onShow(DialogInterface dI) {
                    Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                    positiveButton.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View view) {
                            if (updateFood(food)) {
                                dialog.dismiss();
                            }
                        }
                    });
                    Button negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                    negativeButton.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                        }
                    });
                }
            });
        }
        else {
            dialog.setOnShowListener(new DialogInterface.OnShowListener() {

                @Override
                public void onShow(DialogInterface dI) {
                    Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                    positiveButton.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View view) {
                            if (addFood()) {
                                dialog.dismiss();
                            }
                        }
                    });
                    Button neutralButton = dialog.getButton(AlertDialog.BUTTON_NEUTRAL);
                    neutralButton.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View view) {
                            if (addFood()) {
                                resetFields();
                            }
                        }
                    });
                    Button negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                    negativeButton.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                        }
                    });
                }
            });
        }

        return dialog;
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_add_food;
    }

    @Override
    public int getTitle() {
        return R.string.menu_title_add_food;
    }

    private void resetFields () {
        nameField.setText("");
        resetDatePicker(datePicker);
        numberPicker.setValue(1);
    }

    public boolean addFood() {
        String name = nameField.getText().toString();
        if(checkIfInputEmpty(name)) { return false; }

        Food food = new Food();
        FoodFridge foodFridge = food.getFoodFridge();

        Date date = DateUtils.getDateFromDatePicker(datePicker);
        foodFridge.setOutOfDate(date);
        foodFridge.setQuantity(numberPicker.getValue());
        food.setName(name);
        food.setFridge(mCurrentFridge);

        EventBus.getDefault().post(new FoodCreatedEvent(food));
        //mCurrentFridge.addFood(food);
        //mFridgeProvider.update(mCurrentFridge);
        return true;
    }

    public boolean updateFood(Food food) {
        String name = nameField.getText().toString();
        if(checkIfInputEmpty(name)) { return false; }

        FoodFridge foodFridge = food.getFoodFridge();

        Date date = DateUtils.getDateFromDatePicker(datePicker);
        foodFridge.setOutOfDate(date);
        foodFridge.setQuantity(numberPicker.getValue());
        food.setName(name);

        EventBus.getDefault().post(new FoodUpdatedEvent(food));
        //mFoodProvider.update(food);
        //mFridgeProvider.update(mCurrentFridge);

        return true;
    }

    private boolean checkIfInputEmpty (String name) {
        TextInputLayout tIL = ButterKnife.findById(getDialog(), R.id.name_text_input_layout);
        if (name.isEmpty()) {
            tIL.setError(getResources().getString(R.string.name_mandatory));
            return true;
        }
        tIL.setError("");
        return false;
    }
}
