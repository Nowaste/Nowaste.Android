/*
 * Copyright (c) 2015.
 *
 * "THE BEER-WARE LICENSE" (Revision 42):
 * Quentin Bontemps <q.bontemps@gmail>  , Florian Garnier <reventlov@tuta.io>
 * and Marjorie Déboté <marjorie.debote@free.com> wrote this file.
 * As long as you retain this notice you can do whatever you want with this stuff.
 * If we meet some day, and you think this stuff is worth it, you can buy me a beer in return.
 *
 * No Waste team
 */

package com.yacorso.nowaste.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;

public class NetworkUtil {

    public static final int NOT_CONNECTED = 0;
    public static final int WIFI = 1;
    public static final int MOBILE = 2;

    public static int getConnectionStatus(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        int status = NOT_CONNECTED;

        if (activeNetwork != null) {
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
                status = WIFI;
            if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
                status = MOBILE;
        }
        return status;
    }

    public static String getConnectionStatusString(Context context) {
        String statusString = "No internet connection";
        int connectionStatus = NetworkUtil.getConnectionStatus(context);
        if (connectionStatus == NetworkUtil.WIFI)
            statusString = "Connected to Wifi";
        if (connectionStatus == NetworkUtil.MOBILE)
            statusString = "Connected to Mobile Data";
        return statusString;
    }

    public static boolean networkReachable(Context context) {
        boolean isReachable = false;

        if(getConnectionStatus(context) > 0)
            isReachable = true;

        return isReachable;
    }

    public static String getWifiName(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        String wifiName = wifiManager.getConnectionInfo().getSSID();
        if (wifiName != null) {
            if (!wifiName.contains("unknown ssid") && wifiName.length() > 2) {
                if (wifiName.startsWith("\"") && wifiName.endsWith("\""))
                    wifiName = wifiName.subSequence(1, wifiName.length() - 1).toString();
            } else {
                wifiName = "";
            }
        } else {
            wifiName = "";
        }
        return wifiName;
    }

    public static String getMobileNetworkName(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String networkName = tm.getNetworkOperatorName();
        if (networkName == null) {
            networkName = "";
        }
        return networkName;
    }

    public static String getGateway(Context context) {
        WifiManager wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        return NetworkUtil.intToIp(wm.getDhcpInfo().gateway);
    }

    public static String intToIp(int i) {
        return ((i >> 24) & 0xFF) + "." +
                ((i >> 16) & 0xFF) + "." +
                ((i >> 8) & 0xFF) + "." +
                (i & 0xFF);
    }
}