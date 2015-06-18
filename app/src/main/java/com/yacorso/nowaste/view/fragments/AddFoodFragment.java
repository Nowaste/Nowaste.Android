package com.yacorso.nowaste.view.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;

import com.raizlabs.android.dbflow.sql.language.Select;
import com.yacorso.nowaste.R;
import com.yacorso.nowaste.models.Food;
import com.yacorso.nowaste.models.FoodFridge;
import com.yacorso.nowaste.models.Fridge;
import com.yacorso.nowaste.services.FoodService;
import com.yacorso.nowaste.services.FridgeService;
import com.yacorso.nowaste.utils.LogUtil;
import com.yacorso.nowaste.utils.Utils;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.ButterKnife;

public class AddFoodFragment extends BaseFragment {

    EditText nameField;
    DatePicker datePicker;
    NumberPicker numberPicker;
    Button buttonValidate;
    Button buttonCheck;

    Fridge mCurrentFridge;
    FoodService mFoodService;
    FridgeService mFridgeService;

    public static AddFoodFragment newInstance() { return new AddFoodFragment(); }

    public AddFoodFragment() { }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mFridgeService = new FridgeService();
        mCurrentFridge = mFridgeService.getCurrentFridge();
        //startVoiceRecognitionActivity();

        nameField = ButterKnife.findById(getActivity(), R.id.name_edit_text);
        datePicker = ButterKnife.findById(getActivity(), R.id.outOfDatePicker);
        buttonValidate = ButterKnife.findById(getActivity(), R.id.addFoodButton);

        buttonValidate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFood();
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

    private void startVoiceRecognitionActivity() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Voice recognition Demo...");
        startActivityForResult(intent, 1234);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1234 && resultCode == getActivity().RESULT_OK)
        {
            // Populate the wordsList with the String values the recognition engine thought it heard
            ArrayList<String> matches = data.getStringArrayListExtra(
                    RecognizerIntent.EXTRA_RESULTS);

            String regex = "(.+)"// N'importe quel charactères
                    + "\\s" // Un espace
                    + "([0-9]{1,2})" // Le jour
                    + "\\s" // Un espace
                    + "(janvier|"
                    + "février|fevrier|"
                    + "mars|avril|mai|juin|juillet|"
                    + "aout|août|"
                    + "septembre|octobre|novembre|"
                    + "décembre|decembre)" // Le mois
                    + "\\s?" // Un espace
                    + "([0-9]{2,4})?"; // L'année

            Pattern p = Pattern.compile(regex);


            Log.e("regex", regex);
            ArrayList<String> realMatches = new ArrayList<String>();
            for (String matchStr : matches) {
                Log.e(matchStr, String.valueOf(matchStr.matches(regex)));
                Matcher matcher = p.matcher(matchStr);
                if(matcher.find()){
                    Log.e("Ca match ! ", matcher.group(1));
                    realMatches.add(matchStr);

                    if(matcher.groupCount() >= 3){
                        String product = matcher.group(1);
                        int day = Integer.valueOf(matcher.group(2));
                        String month = matcher.group(3);
                        String year = "";



                        if(matcher.groupCount() == 4){
                            year = matcher.group(4);
                            SecureRandom random = new SecureRandom();
                            Food food = new Food();
                            food.setName(product);
                            FoodFridge foodFridge = food.getFoodFridge();
                            String date = year + "/" + month + "/" + Integer.toString(day);
                            SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
                            Date utilDate = null;
                            try {
                                utilDate = formatter.parse(date);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            foodFridge.setOutOfDate(utilDate);

                            food.save();

                            LogUtil.LOGD("product ", product);
                            LogUtil.LOGD("day ", Integer.toString(day));
                            LogUtil.LOGD("month ", month);
                            LogUtil.LOGD("year ", year);

                            //finish();

                        }
                    }
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void addFood() {

        Food food = new Food();
        FoodFridge foodFridge = food.getFoodFridge();
        Date date = Utils.getDateFromDatePicker(datePicker);
        foodFridge.setOutOfDate(date);
        foodFridge.setQuantity(5);

        List<Fridge> fridgeList = new Select().from(Fridge.class).queryList();
        List<FoodFridge> foodFridgeList = new Select().from(FoodFridge.class).queryList();
        List<Food> foodList = new Select().from(Food.class).queryList();
        food.setFridge(mCurrentFridge);

        food.setName(nameField.getText().toString());

        mCurrentFridge.addFood(food);

        mFridgeService.update(mCurrentFridge);
    }
}
