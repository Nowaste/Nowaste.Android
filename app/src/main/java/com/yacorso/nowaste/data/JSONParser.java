package com.yacorso.nowaste.data;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.raizlabs.android.dbflow.structure.ModelAdapter;
import com.yacorso.nowaste.model.FoodList;
import com.yacorso.nowaste.model.User;

import java.util.List;

public class JSONParser {

    Gson gson;

    public JSONParser() {
        gson = new GsonBuilder()
                .setExclusionStrategies(new ExclusionStrategy() {
                    @Override
                    public boolean shouldSkipField(FieldAttributes f) {
                        return f.getDeclaredClass().equals(ModelAdapter.class);
                    }

                    @Override
                    public boolean shouldSkipClass(Class<?> clazz) {
                        return false;
                    }
                })
                .create();
    }
    public FoodList parseJSONToFoodList (String json) {
        return gson.fromJson(json, FoodList.class);
    }

    public User parseJSONToUser (String json) {
        return gson.fromJson(json, User.class);
    }

}
