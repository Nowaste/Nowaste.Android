package com.yacorso.nowaste;

import com.yacorso.nowaste.models.Food;
import com.yacorso.nowaste.models.FoodFridge;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertNotNull;

@Config(constants = BuildConfig.class, sdk = 21)
@RunWith(RobolectricTestRunner.class)
public class FoodTest {
    Food food;
    FoodFridge foodFridge;

    @Before
    public void setup() throws Exception {
        food = new Food();
    }

    @Test
    public void checkFoodNotNull() throws Exception {
        assertNotNull("Food is not instantiated", food);
    }

    @Test
    public void checkFoodFridgeNotNull() throws Exception {
        foodFridge = food.getFoodFridge();
        assertNotNull("FoodFridge not instantiate", foodFridge);
    }
}
