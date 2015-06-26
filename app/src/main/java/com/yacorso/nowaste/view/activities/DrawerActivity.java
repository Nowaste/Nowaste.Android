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

package com.yacorso.nowaste.view.activities;

import android.content.res.Configuration;
import android.support.annotation.IdRes;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.yacorso.nowaste.R;
import com.yacorso.nowaste.utils.LogUtil;
import com.yacorso.nowaste.utils.NavigatorUtil;
import com.yacorso.nowaste.view.fragments.BaseFragment;
import com.yacorso.nowaste.view.fragments.FoodListFragment;
import com.yacorso.nowaste.view.fragments.NavigationDrawerFragment;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.greenrobot.event.EventBus;

public class DrawerActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener,
        NavigationDrawerFragment.FragmentDrawerListener {

    @InjectView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

//    @InjectView(R.id.navigation_view)
//    NavigationView mNavigationView;

    NavigationDrawerFragment mDrawerFragment;

    //    private static NavigatorUtil mNavigator;
    @InjectView(R.id.toolbar)
    Toolbar mToolbar;
//    private ActionBarDrawerToggle mDrawerToggle;
//    private @IdRes int mCurrentMenuItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);

        ButterKnife.inject(this);

//        mDrawerFragment = (NavigationDrawerFragment)
//                    getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);

        initActionBarAndNavDrawer();
//        initNavigator();

//        mCurrentMenuItem = R.id.menu_item_my_fridge;
//        setNewRootFragment(FoodListFragment.newInstance());
    }

    private void initActionBarAndNavDrawer() {
//        mToolbar = ButterKnife.findById(this, R.id.toolbar);
        setSupportActionBar(mToolbar);

//        mDrawerToggle = new ActionBarDrawerToggle(
//                this, mDrawerLayout,
//                R.string.navigation_drawer_open,
//                R.string.navigation_drawer_close);
//
//        mDrawerToggle.setDrawerIndicatorEnabled(true);
//        mDrawerLayout.setDrawerListener(mDrawerToggle);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

//        mDrawerToggle.syncState();
//        mNavigationView.setNavigationItemSelectedListener(this);

        mDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        mDrawerFragment.setUp(R.id.fragment_navigation_drawer, mDrawerLayout, mToolbar);
        mDrawerFragment.setDrawerListener(this);
        displayView(0);
    }

    private void displayView(int position) {
        Fragment fragment = null;
        String title = getString(R.string.app_name);
        switch (position) {
            case 0:
                fragment = new FoodListFragment();
                title = getString(R.string.app_name);
                break;
//            case 1:
//                fragment = new FriendsFragment();
//                title = getString(R.string.title_friends);
//                break;
//            case 2:
//                fragment = new MessagesFragment();
//                title = getString(R.string.title_messages);
//                break;
            default:
                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body, fragment);
            fragmentTransaction.commit();

            // set the toolbar title
            getSupportActionBar().setTitle(title);
        }
    }

    @Override
    public void onDrawerItemSelected(View view, int position) {

    }

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        return false;
    }
//    private void initNavigator() {
//        if(mNavigator != null) return;
//        mNavigator = new NavigatorUtil(getSupportFragmentManager(), R.id.container);
//    }
//
//    private void setNewRootFragment(BaseFragment fragment){
//        mNavigator.setRootFragment(fragment);
//        getSupportActionBar().setTitle(fragment.getTitle());
//        mDrawerLayout.closeDrawers();
//    }
//
//    @Override
//    protected void onPostCreate(Bundle savedInstanceState) {
//        super.onPostCreate(savedInstanceState);
//        // Sync the toggle state after onRestoreInstanceState has occurred.
//        mDrawerToggle.syncState();
//    }
//
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        if (mDrawerToggle.onOptionsItemSelected(item)) {
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }
//
//    @Override
//    public void onConfigurationChanged(Configuration newConfig) {
//        super.onConfigurationChanged(newConfig);
//        mDrawerToggle.onConfigurationChanged(newConfig);
//    }
//
//
//    @Override
//    public boolean onNavigationItemSelected(MenuItem menuItem) {
//        @IdRes int id = menuItem.getItemId();
//        if(id == mCurrentMenuItem) {
//            mDrawerLayout.closeDrawers();
//            return false;
//        }
//        switch (id){
//            case R.id.menu_item_my_fridge:
//                Toast.makeText(this, "Mon frigo", Toast.LENGTH_SHORT).show();
////                setNewRootFragment(StandardAppBarFragment.newInstance());
//                break;
//            case R.id.menu_item_settings:
//                Toast.makeText(this, "Paramètres", Toast.LENGTH_SHORT).show();
//
////                setNewRootFragment(TabHolderFragment.newInstance());
//                break;
//        }
//        mCurrentMenuItem = id;
//        menuItem.setChecked(true);
//        return false;
//    }

//    @Override
//    public void finish() {
//        mNavigator = null;
//        super.finish();
//    }
//
//    private void openFragment (BaseFragment fragment) {
//        mNavigator.goTo(fragment);
//        getSupportActionBar().setTitle(fragment.getTitle());
//    }
}
