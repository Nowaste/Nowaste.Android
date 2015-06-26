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

package com.yacorso.nowaste.utils;

import android.util.Log;

import com.yacorso.nowaste.BuildConfig;


// This class is copied from the project https://github.com/google/iosched
public class LogUtil {
    private static final String LOG_PREFIX = "nowaste_";
    private static final int LOG_PREFIX_LENGTH = LOG_PREFIX.length();
    private static final int MAX_LOG_TAG_LENGTH = 23;

    public static String makeLogTag(String str) {
        if (str.length() > MAX_LOG_TAG_LENGTH - LOG_PREFIX_LENGTH) {
            return LOG_PREFIX + str.substring(0, MAX_LOG_TAG_LENGTH - LOG_PREFIX_LENGTH - 1);
        }

        return LOG_PREFIX + str;
    }

    /**
         * Don't use this when obfuscating class names!
     */
    public static String makeLogTag(Class cls) {
        return makeLogTag(cls.getSimpleName());
    }

    public static String makeLogTag(Object obj){ return  obj.getClass().getSimpleName();}
    public static void LOGD(final String tag, String message) {
        //noinspection PointlessBooleanExpression,ConstantConditions
        if (BuildConfig.DEBUG  || Log.isLoggable(tag, Log.DEBUG)) {
            Log.d(tag, message);
        }
    }

    public static void LOGD(final Object obj, String message) {
        LOGD(makeLogTag(obj), message);
    }

    public static void LOGD(final String tag, String message, Throwable cause) {
        //noinspection PointlessBooleanExpression,ConstantConditions
        if (BuildConfig.DEBUG || Log.isLoggable(tag, Log.DEBUG)) {
            Log.d(tag, message, cause);
        }
    }

    public static void LOGD(final Object obj, String message, Throwable cause) {
        LOGD(makeLogTag(obj), message,cause);
    }

    public static void LOGV(final String tag, String message) {
        //noinspection PointlessBooleanExpression,ConstantConditions
        if (BuildConfig.DEBUG && Log.isLoggable(tag, Log.VERBOSE)) {
            Log.v(tag, message);
        }
    }

    public static void LOGV(final Object obj, String message) {
        LOGV(makeLogTag(obj), message);
    }

    public static void LOGV(final String tag, String message, Throwable cause) {
        //noinspection PointlessBooleanExpression,ConstantConditions
        if (BuildConfig.DEBUG && Log.isLoggable(tag, Log.VERBOSE)) {
            Log.v(tag, message, cause);
        }
    }

    public static void LOGV(final Object obj, String message, Throwable cause) {
        LOGV(makeLogTag(obj), message, cause);
    }

    public static void LOGI(final String tag, String message) {
        Log.i(tag, message);
    }

    public static void LOGI(final Object obj, String message) {
        LOGI(makeLogTag(obj), message);
    }
    public static void LOGI(final String tag, String message, Throwable cause) {
        Log.i(tag, message, cause);
    }

    public static void LOGI(final Object obj, String message, Throwable cause) {
        LOGI(makeLogTag(obj), message, cause);
    }

    public static void LOGW(final String tag, String message) {
        Log.w(tag, message);
    }

    public static void LOGW(final Object obj, String message) {
        LOGW(makeLogTag(obj), message);
    }

    public static void LOGW(final String tag, String message, Throwable cause) {
        Log.w(tag, message, cause);
    }

    public static void LOGW(final Object obj, String message, Throwable cause) {
        LOGW(makeLogTag(obj), message, cause);
    }

    public static void LOGE(final String tag, String message) {
        Log.e(tag, message);
    }

    public static void LOGE(final Object obj, String message) {
        LOGE(makeLogTag(obj), message);
    }

    public static void LOGE(final String tag, String message, Throwable cause) {
        Log.e(tag, message, cause);
    }

    public static void LOGE(final Object obj, String message, Throwable cause) {
        LOGE(makeLogTag(obj), message, cause);
    }

    private LogUtil() {
    }
}
