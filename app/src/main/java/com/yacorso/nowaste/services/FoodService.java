package com.yacorso.nowaste.services;

import android.content.Context;

import com.yacorso.nowaste.model.Food;
import com.yacorso.nowaste.utils.NetworkUtil;

import java.util.List;

/**
 * Created by quentin on 17/06/15.
 */
public class FoodService extends Service<Food, Integer> {

    public FoodService(Context context) {
        super(context);
    }

    @Override
    public long create(Food item) {
        return 0;
    }

    @Override
    public long update(Food item) {
        return 0;
    }

    @Override
    public void delete(Food item) {

    }

    @Override
    public Food get(Integer id) {
        return null;
    }

    @Override
    public List<Food> all() {
        return null;
    }
}
