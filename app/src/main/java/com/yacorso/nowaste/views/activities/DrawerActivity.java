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

package com.yacorso.nowaste.views.activities;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.yacorso.nowaste.NowasteApplication;
import com.yacorso.nowaste.R;
import com.yacorso.nowaste.events.CallAddFoodEvent;
import com.yacorso.nowaste.events.SetTitleEvent;
import com.yacorso.nowaste.events.CancelSearchEvent;
import com.yacorso.nowaste.events.LaunchSearchEvent;
import com.yacorso.nowaste.events.CallUpdateFoodEvent;
import com.yacorso.nowaste.models.Fridge;
import com.yacorso.nowaste.models.NavigationDrawerItem;
import com.yacorso.nowaste.models.User;
import com.yacorso.nowaste.utils.NavigatorUtil;
import com.yacorso.nowaste.views.fragments.AddFoodFragment;
import com.yacorso.nowaste.views.fragments.BaseFragment;
import com.yacorso.nowaste.views.fragments.FoodListFragment;
import com.yacorso.nowaste.views.fragments.NavigationDrawerFragment;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.greenrobot.event.EventBus;

import static com.yacorso.nowaste.utils.Utils.hideKeyboard;
import static com.yacorso.nowaste.utils.Utils.showKeyboard;

public class DrawerActivity extends AppCompatActivity implements
        NavigationDrawerFragment.FragmentDrawerListener {

    @InjectView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

    NavigationDrawerFragment mDrawerFragment;
    NavigatorUtil mNavigatorUtil;

    @InjectView(R.id.toolbar)
    Toolbar mToolbar;

    private MenuItem mSearchAction;
    private boolean isSearchOpened = false;
    private EditText searchQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);

        ButterKnife.inject(this);

        mNavigatorUtil = new NavigatorUtil(getSupportFragmentManager(), R.id.container_body);

        initActionBarAndNavDrawer();

        if(savedInstanceState == null)
        {
            /*
             * If user has a fridge, display it
             */
            User currentUser = NowasteApplication.getCurrentUser();
            List<Fridge> fridges = currentUser.getFridges();
            if(fridges.size() > 0){
                mNavigatorUtil.setRootFragment(FoodListFragment.newInstance(fridges.get(0)));
            }
            /*
             * Then display speech recognizer
             */
            else{

            }
        }
    }

    private void initActionBarAndNavDrawer() {

        // Set toolbar as actionbar
        setSupportActionBar(mToolbar);

        // Display hamburger button, on the top left
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        // Set up drawer fragment
        mDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        mDrawerFragment.setUp(R.id.fragment_navigation_drawer, mDrawerLayout, mToolbar);
        mDrawerFragment.setDrawerListener(this);
    }


    private void updateToolbarTitle(String title) {
        getSupportActionBar().setTitle(title);
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

    protected void handleMenuSearch() {
        ActionBar action = getSupportActionBar(); //get the actionbar

        if (isSearchOpened) { //test if the search is open

            action.setDisplayShowCustomEnabled(false); //disable a custom view inside the actionbar
            action.setDisplayShowTitleEnabled(true); //show the title in the action bar
            cancelSearch();
            hideKeyboard(this);

            //add the search icon in the action bar
            mSearchAction.setIcon(getResources().getDrawable(R.drawable.abc_ic_search_api_mtrl_alpha));

            isSearchOpened = false;
        } else { //open the search entry

            action.setDisplayShowCustomEnabled(true); //enable it to display a
            // custom view in the action bar.
            action.setCustomView(R.layout.searchbar);//add the custom view
            action.setDisplayShowTitleEnabled(false); //hide the title

            searchQuery = (EditText) action.getCustomView().findViewById(R.id.edtSearch); //the text editor
            //this is a listener to do a search when the user clicks on search button
            searchQuery.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                        hideKeyboard(getParent());
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
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });

            searchQuery.requestFocus();

            showKeyboard(searchQuery, this);

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

    @Override
    public void onBackPressed() {
        if (isSearchOpened) {
            handleMenuSearch();
            return;
        }
        super.onBackPressed();
    }

    @Override
    public void onDrawerItemSelected(View view, int position, NavigationDrawerItem menuItem) {
        mNavigatorUtil.setRootFragment(menuItem.getFragment());
        updateToolbarTitle(menuItem.getTitle());
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

    public void onEvent(SetTitleEvent event) {
        updateToolbarTitle(event.getTitleFragment());
    }

    private void launchDialog(BaseFragment fragment) {
        fragment.show(getSupportFragmentManager(), "dialog");
    }

    public void onEvent(CallAddFoodEvent event) {
        launchDialog(AddFoodFragment.newInstance());
    }
    public void onEvent(CallUpdateFoodEvent event) {
        launchDialog(AddFoodFragment.newInstance(event.getFood()));
    }

    public void onEvent(CancelSearchEvent event) {
        if (isSearchOpened) {
            handleMenuSearch();
        }
    }

}
