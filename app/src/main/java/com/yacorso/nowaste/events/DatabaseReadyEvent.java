package com.yacorso.nowaste.events;

import com.yacorso.nowaste.models.User;

/**
 * Created by fgarnier on 10/07/15.
 */
public class DatabaseReadyEvent {
    private User user;

    public DatabaseReadyEvent(User userElement) {
        user = userElement;
    }

    public User getUser() {
        return user;
    }
}
