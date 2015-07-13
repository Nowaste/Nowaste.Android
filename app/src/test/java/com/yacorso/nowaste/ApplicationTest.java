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
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.raizlabs.android.dbflow.config.FlowManager;
import com.yacorso.nowaste.events.UserCreatedEvent;
import com.yacorso.nowaste.models.Food;
import com.yacorso.nowaste.models.FoodFridge;
import com.yacorso.nowaste.models.FoodList;
import com.yacorso.nowaste.models.User;
import com.yacorso.nowaste.providers.FoodProvider;
import com.yacorso.nowaste.providers.UserProvider;
import com.yacorso.nowaste.utils.DateUtils;
import com.yacorso.nowaste.views.activities.DrawerActivity;
import com.yacorso.nowaste.views.adapters.FoodListAdapter;
import com.yacorso.nowaste.views.fragments.AddFoodFragment;
import com.yacorso.nowaste.views.fragments.BaseFragment;
import com.yacorso.nowaste.views.fragments.FoodListFragment;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.robolectric.Shadows.shadowOf;

@RunWith(CustomRobolectricRunner.class)
@Config(emulateSdk = 21)
public class ApplicationTest {

    FragmentActivity activity;
    FoodListFragment customFragment;

    FoodFridge fridgeFragment;
    FoodList foodList;
    Food food;

    UserProvider userProvider;
    FoodProvider foodProvider;
    User user;
    int countUsers;

    @Before
    public void setup() throws Exception {
        activity = Robolectric.buildActivity(FragmentActivity.class).create().start().resume().get();
        customFragment = new FoodListFragment();



        food = new Food();
        FoodFridge foodFridge = food.getFoodFridge();

        Date date = new Date();
        foodFridge.setOutOfDate(date);
        foodFridge.setQuantity(12);
        food.setName("Test");

        EventBus.getDefault().register(this);
    }

    //test of adding different elements into the database
    @Test
    public void testAddElementToDB () {
        userProvider = new UserProvider();
        countUsers = userProvider.all().size();

        user = new User();
        user.setEmail("toto.albert@nowaste.fr");
        user.setFirstName("Toto");
        user.setLastName("Albert");

        userProvider.create(user);
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        int newCountUsers = userProvider.all().size();
        assertNotEquals(countUsers, newCountUsers);
    }

    //test add food into BDD
    @Test
    public void testAddFoodToDB () {
        foodProvider = new FoodProvider();
        int countFood = foodProvider.all().size();

        food = new Food();
        food.setName("Jambon");
        food.setFoodFridge(fridgeFragment);
        food.setFoodList(foodList);

        foodProvider.create(food);

        //a quoi sert le sleep ?
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        int newCountFood = foodProvider.all().size();

        assertNotEquals(countFood, newCountFood);
    }

    public void onEvent (UserCreatedEvent event) {
        User user = event.getUser();
    }

    //name of food should match string
    @Test
    public void txt_food_name_should_match_string(){
        //Given
        TextView nameField = (TextView)activity.findViewById(R.id.txt_food_name);
        // When
        //boolean result = nameField.matches("jambon");
        // Then
        assertEquals(nameField.getText(), "jambon");
    }

/*  @Test
    public void should_compare_less() {
        // Given
        App app = new App("AAA");

        // When
        int result = app.compareTo(new App("BBB"));

        // Then
        assertThat(result).isEqualTo(-1);
    }*/

    //date out_of_date should be after today
    /*@Test
    public void out_of_date_should_be_today_or_more(){
        //Given
        //TextView out_of_date = (TextView)activity.findViewById(R.id.out_of_date_textview);
        //String title = shadowOf(activity).getResources().getString(customFragment.getTitle());
        SimpleDateFormat today = new SimpleDateFormat("dd/MM/yyy");
        String myDate = today.format(fridgeFragment.getOutOfDate());

        String out_of_date = shadowOf(activity).getResources().getString(myDate);
        //When
        int result = out_of_date.compareTo(myDate);
        //Then
        assertThat(result).isEqualTo(-1);
        //out_of_date = 12/06/2015
    }*/

    @Test
    public void onCreate_shouldInflateLayout() throws Exception {
        final Menu menu = shadowOf(activity).getOptionsMenu();
//        assertThat(menu.findItem(R.id.menu_item_my_fridge).getTitle()).isEqualTo(activity.getString(R.string.menu_title_my_fridge));
    }


    /** txt_food_name **/

    @Test
    public void shouldHaveAppNameAsTitle(){
        //Given
        //TextView nameField = (TextView)activity.findViewById(R.id.txt_food_name);
        // When
        //boolean result = nameField.matches("jambon");
        // Then
        String title = shadowOf(activity).getResources().getString(customFragment.getTitle());
        //String title = customFragment.getResources().getString(customFragment.getTitle());
        assertEquals(title, "No Waste");

    }

    @Test
    public void txt_food_name_should_not_match_null(){
        //Given
        //EditText nameField = (EditText)activity.findViewById(R.id.txt_food_name);
        // When
        //boolean result = nameField.matches(null);
        // Then
        //assertThat(result).isFalse();
    }

    @Test
    public void txt_food_name_should_not_match_empty(){
        //Given
        //EditText nameField = (EditText)activity.findViewById(R.id.txt_food_name);
        // When
        //boolean result = nameField.matches("");
        // Then
        //assertThat(result).isFalse();
    }

    /** btn_food_quantity **/

    //a tester avec une boucle for de plusieurs tests
    @Test
    public void btn_food_quantity_should_match(){
        int max = 99;
        int min = 1;
        int random = (int)Math.random() * (max - min) + min;
        // Given
        //NumberPicker numberPicker = (NumberPicker)activity.findViewById(R.id.btn_food_quantity);
        // When
        //boolean result = numberPicker.matches(random);
        // Then
        //assertThat(result).isTrue();
    }

    @Test
    public void btn_food_quantity_should_not_match_null(){
        //Given
        //NumberPicker numberPicker = (NumberPicker)activity.findViewById(R.id.btn_food_quantity);
        // When
        //boolean result = numberPicker.matches(null);
        // Then
        //assertThat(result).isFalse();
    }

    @Test
    public void btn_food_quantity_should_not_match_empty(){
        //Given
        //NumberPicker numberPicker = (NumberPicker)activity.findViewById(R.id.btn_food_quantity);
        // When
        //boolean result = numberPicker.matches("");
        // Then
        //assertThat(numberPicker.get).isFalse();
        //assertFalse(numberPicker.getValue());
    }

    @Test
    public void btn_food_quantity_should_not_match_zero(){
        //Given
        TextView numberPicker = (TextView)shadowOf(activity).findViewById(R.id.btn_food_quantity);
        // When
        assertNotEquals(Integer.toString(0), numberPicker.getText().toString());

        // Then
        //assertThat(result).isFalse();
    }

    @Test
    public void newInstanceNoDataSetTest(){
        //Initialize a new adapter and test if all the needed fields are created
        FoodListAdapter adapterNoDataSet = new FoodListAdapter();

        //food.setFoodList(mCurrentFridge);

        assertNotNull(adapterNoDataSet);

        //Check to see if the count of the adapter is 0 (no items have been set)
        assertEquals(adapterNoDataSet.getItemCount(), 0);

        adapterNoDataSet.add(food);

        assertEquals(adapterNoDataSet.getItemCount(), 1);
    }

    //a tester avec une boucle for de plusieurs tests
    @Test
    public void btn_food_quantity_should_not_match_more_than_hundred(){
        int min = 100;
        int max = 999999999;
        //Given
        //NumberPicker numberPicker = (NumberPicker)activity.findViewById(R.id.btn_food_quantity);
        // When
        //boolean result = numberPicker.matches(Math.random() * (max - min) + min);
        // Then
        //assertThat(result).isFalse();
    }

}
