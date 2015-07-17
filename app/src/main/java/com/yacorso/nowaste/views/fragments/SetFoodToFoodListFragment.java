package com.yacorso.nowaste.views.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
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
import com.yacorso.nowaste.events.CallSetFoodEvent;
import com.yacorso.nowaste.events.CallSpeechAddFoodEvent;
import com.yacorso.nowaste.models.CustomList;
import com.yacorso.nowaste.models.Food;
import com.yacorso.nowaste.models.FoodFridge;
import com.yacorso.nowaste.models.FoodList;
import com.yacorso.nowaste.models.Fridge;
import com.yacorso.nowaste.providers.FoodProvider;
import com.yacorso.nowaste.utils.DateUtils;

import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

import static com.yacorso.nowaste.utils.DateUtils.resetDatePicker;
import static com.yacorso.nowaste.utils.DateUtils.setDatePicker;

public class SetFoodToFoodListFragment extends BaseFragment {

    @Bind(R.id.name_edit_text) EditText nameField;
    @Bind(R.id.out_of_date_picker) DatePicker datePicker;
    @Bind(R.id.quantity_number_picker) NumberPicker numberPicker;
    @Bind(R.id.name_text_input_layout) TextInputLayout tIL;
    @Bind(R.id.container_items_fridge) View containerFridgeItems;

    FoodList currentFoodList;
    FoodProvider foodProvider;

    public static SetFoodToFoodListFragment newInstance(Food food, FoodList foodList) {
        SetFoodToFoodListFragment setFoodToFoodListFragment = new SetFoodToFoodListFragment();

        Bundle args = new Bundle();
        args.putParcelable("foodList", foodList);
        if (food != null) {
            args.putParcelable("food", food);
        }
        setFoodToFoodListFragment.setArguments(args);

        return setFoodToFoodListFragment;
    }

    public SetFoodToFoodListFragment() {
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle arguments = getArguments();

        if (arguments.containsKey("foodList") ) {
            currentFoodList = arguments.getParcelable("foodList");
        }

        if (currentFoodList instanceof CustomList) {
            containerFridgeItems.setVisibility(View.GONE);
        }

        foodProvider = new FoodProvider();
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
        Food food = null;

        if (arguments != null && arguments.containsKey("food")) {
            food = arguments.getParcelable("food");
        }

        return getFoodDialog(builder, food);
    }

    private AlertDialog getFoodDialog(AlertDialog.Builder builder, Food food) {
        AlertDialog dialog;

        builder.setNegativeButton(R.string.cancel, null);

        if (food == null) {
            builder.setPositiveButton(R.string.add_one_element, null);
            builder.setNeutralButton(R.string.add_another_element, null);
            dialog = setButtonsListener(builder, null);
        }
        else {
            setFoodToBuilder(food);
            builder.setPositiveButton(R.string.validate, null);
            dialog = setButtonsListener(builder, food);
        }

        return dialog;
    }

    private void setFoodToBuilder(Food food) {
        nameField.setText(food.getName());
        if (currentFoodList instanceof Fridge) {
            setDatePicker(datePicker, food.getFoodFridge().getOutOfDate());
            numberPicker.setValue(food.getFoodFridge().getQuantity());
        }
    }

    private AlertDialog setButtonsListener(AlertDialog.Builder builder, Food food) {
        final AlertDialog dialog = builder.create();

        if (food == null) {
            setButtonListenerForCreate(dialog);
        }
        else {
            setButtonListenerForUpdate(dialog, food);
        }

        return dialog;
    }

    private void setButtonListenerForUpdate(final AlertDialog dialog, final Food food) {
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dI) {
                Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positiveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (setFood(food)) {
                            foodProvider.update(food);
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

    private void setButtonListenerForCreate(final AlertDialog dialog) {
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            Food food = new Food();
            @Override
            public void onShow(DialogInterface dI) {
                Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positiveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (setFood(food)) {
                            food.setFoodList(currentFoodList);
                            foodProvider.create(food);
                            dialog.dismiss();
                        }
                    }
                });
                Button neutralButton = dialog.getButton(AlertDialog.BUTTON_NEUTRAL);
                neutralButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (setFood(food)) {
                            food.setFoodList(currentFoodList);
                            foodProvider.create(food);
                            dialog.dismiss();
                            EventBus.getDefault().post(new CallSpeechAddFoodEvent());
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

    public boolean setFood(Food food) {
        String name = nameField.getText().toString();
        if (checkIfInputEmpty(name)) {
            return false;
        }

        if (currentFoodList instanceof Fridge) {
            FoodFridge foodFridge = food.getFoodFridge();
            Date date = DateUtils.getDateFromDatePicker(datePicker);
            foodFridge.setOutOfDate(date);
            foodFridge.setQuantity(numberPicker.getValue());
        }

        food.setName(name);

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
