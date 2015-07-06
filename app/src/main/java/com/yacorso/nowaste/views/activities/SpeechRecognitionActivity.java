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

package com.yacorso.nowaste.views.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.yacorso.nowaste.R;
import com.yacorso.nowaste.utils.LogUtil;
import com.yacorso.nowaste.utils.NavigatorUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by quentin on 05/07/15.
 */
public class SpeechRecognitionActivity extends AppCompatActivity {
    private static final int REQUEST_CODE = 1234;
    private ListView wordsList;
    NavigatorUtil mNavigatorUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        mNavigatorUtil = new NavigatorUtil(getSupportFragmentManager(), R.id.container_body);

        PackageManager pm = getPackageManager();
        List<ResolveInfo> activities = pm.queryIntentActivities(
                new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH),0);
        if (activities.size() > 0)
        {
//            btnSpeak.setEnabled(false);
//            btnSpeak.setText("Recognizer not present");
            startVoiceRecognitionActivity();
        }else{
//            mNavigatorUtil.goOneBack();
        }

    }

    public void speakButtonClicked(View v){
        startVoiceRecognitionActivity();
    }

    /**
     * Fire an intent to start the voice recognition activity.
     */
    private void startVoiceRecognitionActivity()
    {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Voice recognition Demo...");
        startActivityForResult(intent, REQUEST_CODE);
    }

    /**
     * Handle the results from the voice recognition activity.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK)
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


            LogUtil.LOGD(this,"regex : " + regex);
            ArrayList<String> realMatches = new ArrayList<String>();
            for (String matchStr : matches) {
                LogUtil.LOGD(this, matchStr + "  ### " + String.valueOf(matchStr.matches(regex)));
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
                        }
                        System.out.println(product);
                        System.out.println(day);
                        System.out.println(month);
                        System.out.println(year);

//						Log.i("produit",product);
//						Log.i("day", String.valueOf(day));
//						Log.i("month",month);
//						Log.i("year", year);
                    }
                }
            }
            wordsList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
                    realMatches));

        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
