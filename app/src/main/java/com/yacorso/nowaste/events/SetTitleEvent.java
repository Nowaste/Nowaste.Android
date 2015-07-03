package com.yacorso.nowaste.events;

/**
 * Created by florian on 03/07/15.
 */
public class SetTitleEvent {

    private String titleFragment;

    public SetTitleEvent (String title) {
        titleFragment = title;
    }

    public String getTitleFragment() {
        return titleFragment;
    }
}
