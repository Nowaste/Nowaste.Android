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

package com.yacorso.nowaste.data;

import com.raizlabs.android.dbflow.annotation.Database;


/**
 * DBFlow database configuration
 */
@Database(name = NowasteDatabase.NAME, version = NowasteDatabase.VERSION)
public class NowasteDatabase {
    /**
     * Database name
     */
    public static final String NAME = "nowaste";

    /**
     * Database version
     */
    public static final int VERSION = 1;
}
