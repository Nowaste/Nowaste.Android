package com.yacorso.nowaste.events;

/**
 * Created by florian on 02/07/15.
 */
public class LaunchSearchEvent {

    protected String searchQuery;

    public LaunchSearchEvent(String search) { searchQuery = search; }

    public String getSearchQuery() {
        return searchQuery;
    }
}
