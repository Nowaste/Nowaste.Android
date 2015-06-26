/*
 * Copyright (c) 2015.
 *
 * "THE BEER-WARE LICENSE" (Revision 42):
 * <q.bontemps@gmail> , <reventlov@tuta.io> and <marjorie.debote@free.com> wrote this file.
 *  As long as you retain this notice you can do whatever you want with this stuff.
 *  If we meet some day, and you think this stuff is worth it, you can buy me a beer in return.
 *
 * No Waste team
 *
 */

package com.yacorso.nowaste;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.widget.TextView;

import com.yacorso.nowaste.view.activities.DrawerActivity;
import com.yacorso.nowaste.view.fragments.FoodListFragment;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowToast;

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
}
