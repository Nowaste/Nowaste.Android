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

package com.yacorso.nowaste.view.activities;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.yacorso.nowaste.R;
import com.yacorso.nowaste.events.AddFoodEvent;
import com.yacorso.nowaste.utils.LogUtil;
import com.yacorso.nowaste.utils.MessageEvent;
import com.yacorso.nowaste.events.CancelSearchEvent;
import com.yacorso.nowaste.events.LaunchSearchEvent;
import com.yacorso.nowaste.utils.NavigatorUtil;
import com.yacorso.nowaste.view.fragments.AddFoodFragment;
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
    private static NavigatorUtil mNavigator;
    private ActionBarDrawerToggle mDrawerToggle;
    private @IdRes int mCurrentMenuItem;
    private MenuItem mSearchAction;
    private boolean isSearchOpened = false;
    private EditText searchQuery;

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
        BaseFragment fragment = null;
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

        launchFragment(fragment, false);
    }

    private void launchFragment (BaseFragment fragment, boolean addTobackStack) {
        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body, fragment);
            if (addTobackStack) {
                fragmentTransaction.addToBackStack(null);
            }
            fragmentTransaction.commit();

            // set the toolbar title
            getSupportActionBar().setTitle(fragment.getTitle());
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        mSearchAction = menu.findItem(R.id.action_search);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_search) {
            handleMenuSearch();
        }
        return super.onOptionsItemSelected(item);
    }

    protected void handleMenuSearch(){
        ActionBar action = getSupportActionBar(); //get the actionbar

        if(isSearchOpened){ //test if the search is open

            action.setDisplayShowCustomEnabled(false); //disable a custom view inside the actionbar
            action.setDisplayShowTitleEnabled(true); //show the title in the action bar
            cancelSearch();
            hideKeyboard();

            //add the search icon in the action bar
            mSearchAction.setIcon(getResources().getDrawable(R.drawable.ic_search_black_48dp));

            isSearchOpened = false;
        } else { //open the search entry

            action.setDisplayShowCustomEnabled(true); //enable it to display a
            // custom view in the action bar.
            action.setCustomView(R.layout.searchbar);//add the custom view
            action.setDisplayShowTitleEnabled(false); //hide the title

            searchQuery = (EditText)action.getCustomView().findViewById(R.id.edtSearch); //the text editor
            //this is a listener to do a search when the user clicks on search button
            searchQuery.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                        hideKeyboard();
                        return true;
                    }
                    return false;
                }
            });

            searchQuery.addTextChangedListener(new TextWatcher() {
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    doSearch(searchQuery.getText().toString());
                }
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
                @Override
                public void afterTextChanged(Editable s) { }
            });

            searchQuery.requestFocus();

            showKeyboard(searchQuery);

            //add the close icon
            mSearchAction.setIcon(getResources().getDrawable(R.drawable.abc_ic_clear_mtrl_alpha));

            isSearchOpened = true;
        }

    }

    private void doSearch(String searchQuery) {
        EventBus.getDefault().post(new LaunchSearchEvent(searchQuery));
    }

    private void cancelSearch() {
        EventBus.getDefault().post(new CancelSearchEvent());
    }


    private void showKeyboard (EditText editText) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
    }

    private void hideKeyboard () {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
    }

    @Override
    public void onBackPressed() {
        if(isSearchOpened) {
            handleMenuSearch();
            return;
        }
        super.onBackPressed();
    }

    @Override
    public void onDrawerItemSelected(View view, int position) {

    }

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        return false;
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    public void onEvent(AddFoodEvent event){
        launchFragment(AddFoodFragment.newInstance(), true);

    }
}
