package com.yacorso.nowaste.events;

import com.yacorso.nowaste.models.NavigationDrawerItem;
import com.yacorso.nowaste.views.fragments.BaseFragment;

/**
 * Created by fgarnier on 16/07/15.
 */
public class ChangeFragmentEvent {
    NavigationDrawerItem item;

    public ChangeFragmentEvent (NavigationDrawerItem item) {
        this.item = item;
    }

    public NavigationDrawerItem getItem() {
        return item;
    }
}
