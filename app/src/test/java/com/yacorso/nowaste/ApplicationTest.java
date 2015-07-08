/*
 * Copyright (c) 2015.
 *
 * "THE BEER-WARE LICENSE" (Revision 42):
 * Quentin Bontemps <q.bontemps@gmail>  , Florian Garnier <reventlov@tuta.io>
 * and Marjorie Déboté <marjorie.debote@free.com> wrote this file.
 * As long as you retain this notice you can do whatever you want with this stuff.
 * If we meet some day, and you think this stuff is worth it, you can buy me a beer in return.
 *
 * NoWaste team
 */

package com.yacorso.nowaste;

import android.app.Fragment;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.widget.Button;
import android.widget.NumberPicker;

import com.yacorso.nowaste.views.activities.DrawerActivity;
import com.yacorso.nowaste.views.fragments.AddFoodFragment;
import com.yacorso.nowaste.views.fragments.BaseFragment;
import com.yacorso.nowaste.views.fragments.FoodListFragment;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import butterknife.ButterKnife;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.robolectric.Shadows.shadowOf;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class ApplicationTest {

    private DrawerActivity activity;

    @Before
    public void setup() throws Exception {
        activity = Robolectric.setupActivity(DrawerActivity.class);
        assertNotNull("DrawerActivity is not instantiated", activity);
    }

    @Test
    public void onCreate_shouldInflateLayout() throws Exception {
        final Menu menu = shadowOf(activity).getOptionsMenu();
//        assertThat(menu.findItem(R.id.menu_item_my_fridge).getTitle()).isEqualTo(activity.getString(R.string.menu_title_my_fridge));
    }

    @Test
    public void clickingLogin_shouldStartLoginActivity() {
        Intent expectedIntent = new Intent(activity, FragmentActivity.class);
        assertThat(shadowOf(activity).getNextStartedActivity()).isEqualTo(expectedIntent);
    }

    /** btn_food_quantity **/

    //a tester avec une boucle for de plusieurs tests
    @Test
    public void btn_food_quantity_should_match(){
        int max = 99;
        int min = 1;
        int random = (int)Math.random() * (max - min) + min;
        // Given
        NumberPicker numberPicker = (NumberPicker) findViewById(R.id.btn_food_quantity);
        // When
        boolean result = numberPicker.matches(random);
        // Then
        assertThat(result).isTrue();
    }

    @Test
    public void btn_food_quantity_should_not_match_null(){
        //Given
        NumberPicker numberPicker = (NumberPicker) findViewById(R.id.btn_food_quantity);
        // When
        boolean result = numberPicker.matches(null);
        // Then
        assertThat(result).isFalse();
    }

    @Test
    public void btn_food_quantity_should_not_match_empty(){
        //Given
        NumberPicker numberPicker = (NumberPicker) findViewById(R.id.btn_food_quantity);
        // When
        boolean result = numberPicker.matches("");
        // Then
        assertThat(result).isFalse();
    }

    @Test
    public void btn_food_quantity_should_not_match_zero(){
        //Given
        NumberPicker numberPicker = (NumberPicker) findViewById(R.id.btn_food_quantity);
        // When
        boolean result = numberPicker.matches(0);
        // Then
        assertThat(result).isFalse();
    }

    //a tester avec une boucle for de plusieurs tests
    @Test
    public void btn_food_quantity_should_not_match_more_than_hundred(){
        int min = 100;
        int max = 999999999;
        //Given
        NumberPicker numberPicker = (NumberPicker) findViewById(R.id.btn_food_quantity);
        // When
        boolean result = numberPicker.matches(Math.random() * (max - min) + min);
        // Then
        assertThat(result).isFalse();
    }

}
