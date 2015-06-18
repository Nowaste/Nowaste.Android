package com.yacorso.nowaste.utils;

/**
 * Created by florian on 18/06/15.
 */
public class MessageEvent {

    //Android dev say not to use enums

    public static final int ADD_FOOD = 0;
    public static final int DELETE_FOOD = 1;
    public static final int NAVIGATION_MODE_TABS = 2;

    public final int message;

    public MessageEvent(int message) {
        this.message = message;
    }
}
