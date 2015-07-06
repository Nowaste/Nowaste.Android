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

import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.yacorso.nowaste.R;
import com.yacorso.nowaste.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.ButterKnife;

/**
 * Created by quentin on 06/07/15.
 */
public class SpeechAddFoodFragment extends BaseFragment {

    SpeechRecognizer mSpeechRecognizer;
    boolean mIsListening = false;


    Intent mSpeechIntent;

    TextView txtInfos;
    Button btnAction;


    public SpeechAddFoodFragment(){

    }

    public static SpeechAddFoodFragment newInstance(){
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

    private void startSpeechRecognizer(){

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

                LogUtil.LOGD("SpeechAddFoodFragment", "onBufferReceived");
                LogUtil.LOGD("SpeechAddFoodFragment", String.valueOf(buffer));
            }

            @Override
            public void onEndOfSpeech() {
                LogUtil.LOGD("SpeechAddFoodFragment", "onEndOfSpeech");
//                mIsListening = false;
            }

            @Override
            public void onError(int error) {

                LogUtil.LOGD("SpeechAddFoodFragment", "onError");
                LogUtil.LOGD("SpeechAddFoodFragment", String.valueOf(error));
                txtInfos.setText(R.string.speech_error);

                mIsListening = false;
            }

            @Override
            public void onResults(Bundle results) {

                LogUtil.LOGD("SpeechAddFoodFragment", "onResults");

                ArrayList<String> resultats = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                LogUtil.LOGD("SpeechAddFoodFragment", resultats.toString());

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


                for (String matchStr : resultats) {
                    LogUtil.LOGD(this, matchStr + "  ### " + String.valueOf(matchStr.matches(regex)));
                    Matcher matcher = p.matcher(matchStr);
                    if(matcher.find()){
                        Log.e("Ca match ! ", matcher.group(1));
                        int count = matcher.groupCount();
                        ArrayList<String> groups = new ArrayList<String>();
                        for (int i = 0; i <= count; i++)
                        {
                            groups.add(matcher.group(i));
                        }

                        realMatches.add(matchStr);

                        if(matcher.groupCount() >= 4){
                            int quantity=1;

                            if(! matcher.group(1).isEmpty()){
                                quantity = Integer.valueOf(matcher.group(1));
                            }

                            String product = matcher.group(2);
                            int day = Integer.valueOf(matcher.group(3));
                            String month = matcher.group(4);
                            String year = "";



                            if(matcher.groupCount() == 5){
                                year = matcher.group(5);
                            }

                            LogUtil.LOGD("SpeechAddFoodFragment", String.valueOf(quantity));
                            LogUtil.LOGD("SpeechAddFoodFragment", product);
                            LogUtil.LOGD("SpeechAddFoodFragment", String.valueOf(day));
                            LogUtil.LOGD("SpeechAddFoodFragment", month);
                            LogUtil.LOGD("SpeechAddFoodFragment", year);

//						Log.i("produit",product);
//						Log.i("day", String.valueOf(day));
//						Log.i("month",month);
//						Log.i("year", year);
                            break;
                        }
                    }
                }


                LogUtil.LOGD("SpeechAddFoodFragment", realMatches.toString());
                txtInfos.setText("C'est terminé");

                mIsListening = false;



            }

            @Override
            public void onPartialResults(Bundle partialResults) {

                LogUtil.LOGD("SpeechAddFoodFragment", "onPartialResults");

                ArrayList<String> resultats = partialResults.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);

                LogUtil.LOGD("SpeechAddFoodFragment", resultats.toString());
            }

            @Override
            public void onEvent(int eventType, Bundle params) {

                LogUtil.LOGD("SpeechAddFoodFragment", "onEvent");
                LogUtil.LOGD("SpeechAddFoodFragment", String.valueOf(eventType));
                LogUtil.LOGD("SpeechAddFoodFragment", params.toString());
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
//        return 0;
        return R.layout.fragment_speech_add_food;
    }

}
