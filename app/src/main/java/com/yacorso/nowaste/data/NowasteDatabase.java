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
