package com.yacorso.nowaste;

import com.yacorso.nowaste.model.Food;
import com.yacorso.nowaste.model.FoodFridge;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.lang.reflect.Field;

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
        //Put all parameters here
        /*Field[] fields = Food.class.getDeclaredFields();
        for (Field field : fields) {
            assertNotNull("Food is not instantiated", field);
        }*/

        //assertNotNull("Food is not instantiated", food.getId());
        //assertNotNull("Food is not instantiated", food.getName());
    }

    @Test
    public void checkFoodFridgeNotNull() throws Exception {
        foodFridge = food.getFoodFridge();
        assertNotNull("FoodFridge not instantiate", foodFridge);
        //assertTrue("TextView contains incorrect text", "Hello world!".equals(tvHelloWorld.getText().toString()));
    }

    @Test
    public void checkFoodFridgeType() throws Exception {
        //Date date = foodFridge.getOutOfDate();
        //assertTrue("TextView contains incorrect text", "Hello world!".equals(tvHelloWorld.getText().toString()));
        //assertNull("FoodFridge Date type incorrect", date);
    }
}
