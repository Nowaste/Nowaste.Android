package com.yacorso.nowaste.data;

import com.raizlabs.android.dbflow.annotation.Database;

@Database(name = NowasteDatabase.NAME, version = NowasteDatabase.VERSION)
public class NowasteDatabase {
    public static final String NAME = "nowaste";
    public static final int VERSION = 1;
}
