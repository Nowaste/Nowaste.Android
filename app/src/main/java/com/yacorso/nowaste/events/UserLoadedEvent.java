package com.yacorso.nowaste.events;

import com.yacorso.nowaste.models.User;

/**
 * Created by quentin on 24/06/15.
 */
public class UserLoadedEvent {

    User mUser;

    public UserLoadedEvent(User mUser) {
        mUser = mUser;
    }

    public User getUser() {
        return mUser;
    }
}
