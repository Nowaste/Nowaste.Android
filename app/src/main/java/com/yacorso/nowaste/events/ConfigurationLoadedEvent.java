/*
 * Copyright (c) 2015.
 *
 * "THE BEER-WARE LICENSE" (Revision 42):
 * Quentin Bontemps <q.bontemps@gmail> , Florian Garnier <reventlov@tuta.io>
 * and Marjorie Deboté <marjorie.debote@free.com> wrote this file.
 * As long as you retain this notice you can do whatever you want with this stuff.
 * If we meet some day, and you think this stuff is worth it, you can buy me a beer in return.
 *
 * NoWaste team
 */

package com.yacorso.nowaste.events;

import com.yacorso.nowaste.models.Configuration;
import com.yacorso.nowaste.models.User;

/**
 * Created by quentin on 24/06/15.
 */
public class ConfigurationLoadedEvent {

    Configuration mConfig;

    public ConfigurationLoadedEvent(Configuration mConfig) {
        mConfig = mConfig;
    }

    public Configuration getConfiguration() {
        return mConfig;
    }
}
