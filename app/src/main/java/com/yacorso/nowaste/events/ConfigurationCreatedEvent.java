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

public class ConfigurationCreatedEvent {
    Configuration config;

    public ConfigurationCreatedEvent(Configuration configElement) {
        config = configElement;
    }

    public Configuration getConfig() {
        return config;
    }
}
