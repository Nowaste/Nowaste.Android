/*
 * Copyright (c) 2015.
 *
 * "THE BEER-WARE LICENSE" (Revision 42):
 * Quentin Bontemps <q.bontemps@gmail>  , Florian Garnier <reventlov@tuta.io>
 * and Marjorie Déboté <marjorie.debote@free.com> wrote this file.
 * As long as you retain this notice you can do whatever you want with this stuff.
 * If we meet some day, and you think this stuff is worth it, you can buy me a beer in return.
 *
 * NoWaste team
 */

package com.yacorso.nowaste.views.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.yacorso.nowaste.R;
import com.yacorso.nowaste.events.SpeechFoodMatchEvent;
import com.yacorso.nowaste.models.Food;
import com.yacorso.nowaste.models.FoodFridge;
import com.yacorso.nowaste.utils.DateUtils;
import com.yacorso.nowaste.utils.LogUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.greenrobot.event.EventBus;

/**
 * Created by quentin on 06/07/15.
 */
public class SpeechAddFoodFragment extends BaseFragment {

    SpeechRecognizer mSpeechRecognizer;
    boolean mIsListening = false;


    Intent mSpeechIntent;

    TextView txtInfos;
    Button btnAction;


    public SpeechAddFoodFragment() {

    }

    public static SpeechAddFoodFragment newInstance() {
        return new SpeechAddFoodFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = super.onCreateView(inflater, container, savedInstanceState);

        mSpeechIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        mSpeechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);

        txtInfos = (TextView) view.findViewById(R.id.txt_speech_infos);
        btnAction = (Button) view.findViewById(R.id.btn_speech_action);
        btnAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mIsListening) {
                    /*
                     * If already listening
                     * Then Stop
                     */
                    mSpeechRecognizer.stopListening();
                } else {
                    /*
                     * If not listening
                     * Then start
                     */
                    mSpeechRecognizer.startListening(mSpeechIntent);
                }
            }
        });


//        ComponentName componentName = new ComponentName("com.google.android.voicesearch",
//                "com.google.android.voicesearch.VoiceSearchPreferences");

        mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(getActivity());

        startSpeechRecognizer();

        return view;
    }

    private void startSpeechRecognizer() {

        RecognitionListener listener = new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle params) {
                LogUtil.LOGD("SpeechAddFoodFragment", "onReadyForSpeech");
                LogUtil.LOGD("SpeechAddFoodFragment", params.toString());
                txtInfos.setText(R.string.speech_speak_now);
            }

            @Override
            public void onBeginningOfSpeech() {
                LogUtil.LOGD("SpeechAddFoodFragment", "onBeginningOfSpeech");
                txtInfos.setText(R.string.speech_speak_now);
                mIsListening = true;
            }

            @Override
            public void onRmsChanged(float rmsdB) {
                LogUtil.LOGD("SpeechAddFoodFragment", "onRmsChanged");
                LogUtil.LOGD("SpeechAddFoodFragment", String.valueOf(rmsdB));
            }

            @Override
            public void onBufferReceived(byte[] buffer) {
            }

            @Override
            public void onEndOfSpeech() {
            }

            @Override
            public void onError(int error) {

                LogUtil.LOGD("SpeechAddFoodFragment", "onError");
                LogUtil.LOGD("SpeechAddFoodFragment", String.valueOf(error));

                int textError = R.string.speech_error_default;

                switch (error) {
                    case SpeechRecognizer.ERROR_AUDIO:
                        textError = R.string.speech_error_audio;
                        break;
                    case SpeechRecognizer.ERROR_NO_MATCH:
                        textError = R.string.speech_error_no_match;
                        break;
                }

                txtInfos.setText(textError);

                mIsListening = false;
            }

            @Override
            public void onResults(Bundle results) {

                ArrayList<String> resultats = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);

                txtInfos.setText(R.string.speech_please_waiting);

                String regex =
                        "([0-9]*)"
                                + "\\s*"
                                + "(.+)"// N'importe quel charactères
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

                LogUtil.LOGD("SpeechAddFoodFragment", "regex : " + regex);

                Pattern p = Pattern.compile(regex);

                ArrayList<String> realMatches = new ArrayList<String>();


                Food foodMatch = null;

                for (String matchStr : resultats) {
                    LogUtil.LOGD(this, matchStr + "  ### " + String.valueOf(matchStr.matches(regex)));
                    Matcher matcher = p.matcher(matchStr);
                    if (matcher.find()) {
                        Log.e("Ca match ! ", matcher.group(1));
                        int count = matcher.groupCount();
                        ArrayList<String> groups = new ArrayList<String>();
                        for (int i = 0; i <= count; i++) {
                            groups.add(matcher.group(i));
                        }

                        realMatches.add(matchStr);

                        if (matcher.groupCount() >= 4) {
                            int quantity = 1;

                            if (!matcher.group(1).isEmpty()) {
                                quantity = Integer.valueOf(matcher.group(1));
                            }

                            String product = matcher.group(2);
                            String array[] = product.split(" ", 2);

                            if (array.length > 1) {

                                switch (array[0]) {
                                    case "un":
                                    case "une":
                                        quantity = 1;
                                        product = array[1];
                                        break;
                                    case "de":
                                    case "deux":
                                        quantity = 2;
                                        product = array[1];
                                        break;
                                }
                            }

                            String day = matcher.group(3);
                            String month = matcher.group(4);
                            String year = "";


                            if (matcher.groupCount() == 5) {
                                year = matcher.group(5);
                            }

                            if (year == null || year.isEmpty()) {
                                year = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
                            }

                            LogUtil.LOGD("SpeechAddFoodFragment", String.valueOf(quantity));
                            LogUtil.LOGD("SpeechAddFoodFragment", product);
                            LogUtil.LOGD("SpeechAddFoodFragment", day);
                            LogUtil.LOGD("SpeechAddFoodFragment", month);
                            LogUtil.LOGD("SpeechAddFoodFragment", year);

                            foodMatch = new Food();
                            foodMatch.setName(product);

                            FoodFridge foodFridgeMatch = new FoodFridge();
                            foodFridgeMatch.setOpen(false);
                            foodFridgeMatch.setQuantity(quantity);

                            String dateString = day + " " + month + " " + year;
                            Date date = DateUtils.getDateFromText(dateString);

                            foodFridgeMatch.setOutOfDate(date);

                            foodMatch.setFoodFridge(foodFridgeMatch);

                            break;
                        }
                    }
                }

                mIsListening = false;


                if (foodMatch != null) {
                    LogUtil.LOGD("SpeechAddFoodFragment", foodMatch.toString());
                    EventBus.getDefault().post(new SpeechFoodMatchEvent(foodMatch));
                    getDialog().dismiss();
                } else {
                    txtInfos.setText(R.string.speech_no_found);
                }

            }

            @Override
            public void onPartialResults(Bundle partialResults) {
            }

            @Override
            public void onEvent(int eventType, Bundle params) {
            }
        };

        mSpeechRecognizer.setRecognitionListener(listener);

        mSpeechRecognizer.startListening(mSpeechIntent);

    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        mSpeechRecognizer.destroy();
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_speech_add_food;
    }

}
