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

package com.yacorso.nowaste.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;

import com.yacorso.nowaste.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateUtils {

    public static Date getDateFromDatePicker(DatePicker datePicker) {
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year = datePicker.getYear();

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);

        return calendar.getTime();
    }


    public static Date getDateFromText(String dateString) {

        DateFormat format = new SimpleDateFormat("d MMMM yyyy",Locale.FRANCE);
        Date date = new Date();
        try {
            date = format.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static String getDateTextFromDate(Date date) {
        String format = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.FRANCE);
        return sdf.format(date);
    }

    public static Date addDaysToDate(Date date, int days) {
        return addOrRemoveDaysToDate(date, days);
    }

    public static Date removeDaysToDate(Date date, int days) {
        return addOrRemoveDaysToDate(date, -days);
    }

    private static Date addOrRemoveDaysToDate(Date date, int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, days);

        Calendar today = Calendar.getInstance();
        today.setTime(new Date());

        if (calendar.before(today)) {
            return today.getTime();
        }

        return calendar.getTime();
    }

    public static void resetDatePicker(DatePicker datePicker) {
        setDatePicker(datePicker, new Date());
    }

    public static void setDatePicker(DatePicker datePicker, Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        datePicker.updateDate(year, month, day);
    }

    public static void setColorCircleFromDate(View view, Date date, Resources res) {
        GradientDrawable bgShape = (GradientDrawable) view.getBackground();
        int color = getColorFromDate(date, res);
        bgShape.setColor(color);
    }

    public static int getColorFromDate(Date date, Resources res) {
        Calendar dateOfElement = Calendar.getInstance();
        dateOfElement.setTime(date);

        Calendar definedDate = Calendar.getInstance();
        definedDate.setTime(new Date());

        int[] colors = {
                res.getColor(R.color.circle_urgent),
                res.getColor(R.color.circle_soon),
                res.getColor(R.color.circle_ok),
                res.getColor(R.color.circle_long)
        };

        for (int color : colors) {
            definedDate.add(Calendar.DATE, 5);
            if (dateOfElement.before(definedDate)) {
                return color;
            }
        }
        return colors[3];
    }

    public static boolean checkIfOutOfDateIsSoon(Date date) {
        Calendar outOfDate = Calendar.getInstance();
        outOfDate.setTime(date);

        Calendar soonDate = Calendar.getInstance();
        soonDate.setTime(new Date());
        soonDate.add(Calendar.DATE, 4);
        if (outOfDate.before(soonDate)) {
            return true;
        }
        return false;
    }

}
