package data;

import com.raizlabs.android.dbflow.annotation.Database;

@Database(name = FoodDatabase.NAME, version = FoodDatabase.VERSION)
public class FoodDatabase {
    public static final String NAME = "food";
    public static final int VERSION = 1;
}
