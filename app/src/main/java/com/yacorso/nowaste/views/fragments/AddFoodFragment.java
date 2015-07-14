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
import com.yacorso.nowaste.events.CallAddFoodEvent;
import com.yacorso.nowaste.events.FoodCreatedEvent;
import com.yacorso.nowaste.events.FoodUpdatedEvent;
import com.yacorso.nowaste.events.SpeechAddFoodEvent;
import com.yacorso.nowaste.models.Food;
import com.yacorso.nowaste.models.FoodFridge;
import com.yacorso.nowaste.models.Fridge;
import com.yacorso.nowaste.models.User;
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

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

import static com.yacorso.nowaste.utils.DateUtils.resetDatePicker;
import static com.yacorso.nowaste.utils.DateUtils.setDatePicker;

public class AddFoodFragment extends BaseFragment {

    public static int TYPE_CREATE = 1;
    public static int TYPE_UPDATE = 2;

    @Bind(R.id.name_edit_text) EditText nameField;
    @Bind(R.id.out_of_date_picker) DatePicker datePicker;
    @Bind(R.id.quantity_number_picker) NumberPicker numberPicker;
    @Bind(R.id.name_text_input_layout) TextInputLayout tIL;

    Fridge mCurrentFridge;
    FoodProvider mFoodProvider;
    FridgeProvider mFridgeProvider;

    public static AddFoodFragment newInstance() {
        return new AddFoodFragment();
    }

    public static AddFoodFragment newInstance(User user, Food food, int type) {
        AddFoodFragment addFoodFragment = new AddFoodFragment();

        Bundle args = new Bundle();
        args.putParcelable("user", user);
        args.putParcelable("food", food);
        args.putInt("type", type);
        addFoodFragment.setArguments(args);

        return addFoodFragment;
    }

    public AddFoodFragment() {
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle arguments = getArguments();
        User user = null;
        if (arguments != null && arguments.containsKey("user")) {
            user = getArguments().getParcelable("user");
        }
        mFridgeProvider = new FridgeProvider();
        mFoodProvider = new FoodProvider();
        mCurrentFridge = mFridgeProvider.getCurrentFridge(user);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return null;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        mRootView = getActivity().getLayoutInflater().inflate(getLayout(), null);
        ButterKnife.bind(this, mRootView);

        Dialog dialog = setDialog();

        return dialog;
    }

    private Dialog setDialog() {

        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(100);

        nameField.requestFocus();
        nameField.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                return false;
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(mRootView);


        Bundle arguments = getArguments();

        AlertDialog dialog = setInformationsToDialog(builder, arguments);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);


        return dialog;

    }

    private AlertDialog setInformationsToDialog(AlertDialog.Builder builder, Bundle arguments) {

        AlertDialog dialog = null;
        int type = TYPE_CREATE;
        Food food = null;

        if (arguments != null && arguments.containsKey("food")) {
            food = arguments.getParcelable("food");
            if (arguments.containsKey("type")) {
                type = arguments.getInt("type");
            }
        }

        if (type == TYPE_UPDATE) {
            dialog = getUpdateFoodDialog(builder, food);

        } else {
            dialog = getCreateFoodDialog(builder, food);
        }

        return dialog;
    }

    private AlertDialog setButtonsListener(AlertDialog.Builder builder, int type, Food food) {
        final AlertDialog dialog = builder.create();

        if (type == TYPE_UPDATE) {
            setButtonListerForUpdate(dialog, food);
        } else {
            setButtonListerForCreate(dialog);
        }

        return dialog;
    }

    private AlertDialog getCreateFoodDialog(AlertDialog.Builder builder, Food food) {
        AlertDialog dialog = null;

        if (food != null) {
            setFoodToBuilder(builder, food);
        }

        builder.setTitle(getTitle());
        builder.setPositiveButton(R.string.add_one_element, null);
        builder.setNeutralButton(R.string.add_another_element, null);
        builder.setNegativeButton(R.string.cancel, null);

        dialog = setButtonsListener(builder, TYPE_CREATE, null);

        return dialog;
    }

    private AlertDialog getUpdateFoodDialog(AlertDialog.Builder builder, Food food) {
        AlertDialog dialog = null;

        if (food != null) {
            builder.setTitle(food.getName());
            setFoodToBuilder(builder, food);
        }

        builder.setPositiveButton(R.string.validate, null);
        builder.setNeutralButton(null, null);
        builder.setNegativeButton(R.string.cancel, null);

        dialog = setButtonsListener(builder, TYPE_UPDATE, food);

        return dialog;
    }

    private void setFoodToBuilder(AlertDialog.Builder builder, Food food) {
        nameField.setText(food.getName());
        setDatePicker(datePicker, food.getFoodFridge().getOutOfDate());
        numberPicker.setValue(food.getFoodFridge().getQuantity());
    }

    private void setButtonListerForUpdate(final AlertDialog dialog, final Food food) {
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

    private void setButtonListerForCreate(final AlertDialog dialog) {
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
                            EventBus.getDefault().post(new CallAddFoodEvent());
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


    @Override
    protected int getLayout() {
        return R.layout.fragment_add_food;
    }

    @Override
    public int getTitle() {
        return R.string.menu_title_add_food;
    }

    private void resetFields() {
        nameField.setText("");
        resetDatePicker(datePicker);
        numberPicker.setValue(1);
    }

    public boolean addFood() {
        String name = nameField.getText().toString();
        if (checkIfInputEmpty(name)) {
            return false;
        }

        Food food = new Food();
        FoodFridge foodFridge = food.getFoodFridge();

        Date date = DateUtils.getDateFromDatePicker(datePicker);
        foodFridge.setOutOfDate(date);
        foodFridge.setQuantity(numberPicker.getValue());
        food.setName(name);
        food.setFoodList(mCurrentFridge);

        mFoodProvider.create(food);
        return true;
    }

    public boolean updateFood(Food food) {
        String name = nameField.getText().toString();
        if (checkIfInputEmpty(name)) {
            return false;
        }

        FoodFridge foodFridge = food.getFoodFridge();

        Date date = DateUtils.getDateFromDatePicker(datePicker);
        foodFridge.setOutOfDate(date);
        foodFridge.setQuantity(numberPicker.getValue());
        food.setName(name);

        mFoodProvider.update(food);

        return true;
    }

    private boolean checkIfInputEmpty(String name) {
        if (name.isEmpty()) {
            tIL.setError(getResources().getString(R.string.name_mandatory));
            return true;
        }
        tIL.setError("");
        return false;
    }
}
