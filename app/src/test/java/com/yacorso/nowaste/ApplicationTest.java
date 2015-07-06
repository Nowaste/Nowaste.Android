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
//    @Test
//    public void clickingButton_shouldChangeResultsViewText() throws Exception {
//        Activity activity = Robolectric.setupActivity(MyActivity.class);
//
//        Button pressMeButton = (Button) activity.findViewById(R.id.press_me_button);
//        TextView results = (TextView) activity.findViewById(R.id.results_text_view);
//
//        pressMeButton.performClick();
//        String resultsText = results.getText().toString();
//        assertThat(resultsText, equalTo("Testing Android Rocks!"));
//    }
    @Test
    public void clickingMike_shouldOpenBoxAddFood() throws Exception {
        setup();

        /**
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.welcome_activity);

            final View button = findViewById(R.id.login);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));
                }
            });
        }

         @Test
         public void clickingLogin_shouldStartLoginActivity() {
             WelcomeActivity activity = Robolectric.setupActivity(WelcomeActivity.class);
             activity.findViewById(R.id.login).performClick();

             Intent expectedIntent = new Intent(activity, WelcomeActivity.class);
             assertThat(shadowOf(activity).getNextStartedActivity()).isEqualTo(expectedIntent);
         }
    **/
        //ButterKnife mFabButton = (ButterKnife) ButterKnife.findViewById(R.id.btnAddFood);

        activity.findViewById(R.id.btnAddFood).performClick();
        Intent expectedIntent = new Intent(activity, FragmentActivity.class);
        assertThat(shadowOf(activity).getNextStartedActivity()).isEqualTo(expectedIntent);
    }
}
