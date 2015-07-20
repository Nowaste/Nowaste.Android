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

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;

import com.yacorso.nowaste.R;
import com.yacorso.nowaste.events.CallCreateFoodEvent;
import com.yacorso.nowaste.events.CallSpeechAddFoodEvent;
import com.yacorso.nowaste.events.CallUpdateFoodEvent;
import com.yacorso.nowaste.events.CustomListCreatedEvent;
import com.yacorso.nowaste.events.FridgeCreatedEvent;
import com.yacorso.nowaste.events.CancelSearchEvent;
import com.yacorso.nowaste.events.LaunchSearchEvent;
import com.yacorso.nowaste.events.SpeechFoodMatchEvent;
import com.yacorso.nowaste.events.UserCreatedEvent;
import com.yacorso.nowaste.models.CustomList;
import com.yacorso.nowaste.models.FoodList;
import com.yacorso.nowaste.models.Fridge;
import com.yacorso.nowaste.models.User;
import com.yacorso.nowaste.providers.CustomListProvider;
import com.yacorso.nowaste.providers.FridgeProvider;
import com.yacorso.nowaste.providers.UserProvider;
import com.yacorso.nowaste.services.AlarmReceiver;
import com.yacorso.nowaste.services.BootCompletedReceiver;
import com.yacorso.nowaste.services.NotificationService;
import com.yacorso.nowaste.utils.NavigatorUtil;
import com.yacorso.nowaste.views.fragments.SetFoodToFoodListFragment;
import com.yacorso.nowaste.views.fragments.BaseFragment;
import com.yacorso.nowaste.views.fragments.FoodListFragment;
import com.yacorso.nowaste.views.fragments.SettingsFragment;
import com.yacorso.nowaste.views.fragments.SpeechAddFoodFragment;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import butterknife.*;
import de.greenrobot.event.EventBus;

public class DrawerActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    public static final int TYPE_CREATE = 1;
    public static final int TYPE_UPDATE = 2;

    @Bind(R.id.drawer_layout) DrawerLayout mDrawerLayout;
    @Bind(R.id.main_navigation) NavigationView navigationView;
    ActionBarDrawerToggle mDrawerToggle;
    NavigatorUtil mNavigatorUtil;
    @Bind(R.id.toolbar) Toolbar mToolbar;

    private MenuItem mSearchAction;
    FoodList currentFoodList;
    FridgeProvider fridgeProvider;
    CustomListProvider customListProvider;
    UserProvider userProvider;
    Map<Integer, FoodList> navigationItems;
    SharedPreferences settings;
    SearchView mSearchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);

        ButterKnife.bind(this);

        mNavigatorUtil = new NavigatorUtil(getSupportFragmentManager(), R.id.container_body);

        fridgeProvider = new FridgeProvider();
        customListProvider = new CustomListProvider();
        userProvider = new UserProvider();
        navigationItems = new LinkedHashMap<>();

        User user;
        settings = getSharedPreferences("preferences", MODE_PRIVATE);
        long userId = settings.getLong("user", 0);

        if(userId == 0){
            /**
             * Load database and call initApp after it is done
             */
            loadDatabase();
        }
        else{
            user = userProvider.get(userId);
            initApp(user);
        }
    }

    public void initApp(User user) {
        initToolbar();

        initNavDrawerAndNavItems(user);

        changeFragment(0, true);

        enableNotificationReceiver();
    }



    /**
     * Setup toolbar
     */
    private void initToolbar() {
        // Set toolbar as actionbar
        setSupportActionBar(mToolbar);

        // Display hamburger button, on the top left
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
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
        mSearchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        mSearchView.setOnQueryTextListener(this);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_search) {
            mSearchAction.expandActionView();
        }
        return super.onOptionsItemSelected(item);
    }



    /**
     * Setup navigation drawer
     */
    private void initNavDrawerAndNavItems(User user) {
        int index = 0;

        Menu navigationMenu = navigationView.getMenu();
        SubMenu fridgeSection = navigationMenu.addSubMenu(R.string.menu_section_fridges);
        //fridgeSection.setHeaderIcon(ContextCompat.getDrawable(this, R.drawable.ic_fridge));
        fridgeSection.setIcon(R.drawable.ic_fridge);
        List<Fridge> fridges = user.getFridges();
        for (Fridge fridge : fridges) {
            navigationItems.put(index, fridge);
            fridgeSection.add(0, index++, 0, fridge.getName());
        }

        SubMenu customListSection = navigationMenu.addSubMenu(R.string.menu_section_custom_lists);
        //customListSection.setHeaderIcon(ContextCompat.getDrawable(this, R.drawable.ic_folder));
        List<CustomList> customLists = user.getCustomLists();

        for (CustomList customList : customLists) {
            navigationItems.put(index, customList);
            customListSection.add(1, index++, 0, customList.getName());
        }

        navigationMenu.add(2, index, 0, R.string.menu_title_settings);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                boolean isFoodList;
                if (menuItem.getGroupId() > 1) {
                    isFoodList = false;
                }
                else {
                    isFoodList = true;
                }
                changeFragment(menuItem.getItemId(), isFoodList);
                return true;
            }
        });

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                mToolbar, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                mToolbar.setAlpha(1 - slideOffset / 2);
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });
    }



    /**
     * Events to change fragment/dialog displayed
     */
    private void changeFragment(int id, boolean isFoodList) {
        BaseFragment fragment;
        String title;

        if (isFoodList) {
            FoodList foodList = navigationItems.get(id);
            fragment = FoodListFragment.newInstance(foodList);
            title = foodList.getName();
            currentFoodList = foodList;
        }
        else {
            fragment = SettingsFragment.newInstance();
            title = getText(R.string.menu_title_settings).toString();
        }

        mNavigatorUtil.setRootFragment(fragment);
        mDrawerLayout.closeDrawer(navigationView);
        updateToolbarTitle(title);
    }

    public void onEvent(CallSpeechAddFoodEvent event) {
        launchDialog(SpeechAddFoodFragment.newInstance());
    }

    public void onEvent(CallCreateFoodEvent event) {
        launchDialog(SetFoodToFoodListFragment.newInstance(event.getFood(), currentFoodList, TYPE_CREATE));
    }

    public void onEvent(CallUpdateFoodEvent event) {
        launchDialog(SetFoodToFoodListFragment.newInstance(event.getFood(), currentFoodList, TYPE_UPDATE));
    }

    public void onEvent(SpeechFoodMatchEvent event) {
        launchDialog(SetFoodToFoodListFragment.newInstance(event.getFood(), currentFoodList , TYPE_CREATE));
    }

    private void launchDialog(BaseFragment fragment) {
        fragment.show(getSupportFragmentManager(), "dialog");
    }



    /**
     * Handle search
     */
    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (newText.isEmpty()) {
            EventBus.getDefault().post(new CancelSearchEvent());
        }
        else {
            EventBus.getDefault().post(new LaunchSearchEvent(newText));
        }
        return false;
    }


    public void onEvent(CancelSearchEvent event) {
        mSearchView.setQuery("", true);
    }

    /**
     * Enable every day local notification
     */
    private void enableNotificationReceiver() {
        Context context = getApplicationContext();
        ComponentName receiver = new ComponentName(context, BootCompletedReceiver.class);
        PackageManager pm = context.getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);

        Intent serviceIntent = new Intent(context, NotificationService.class);
        context.stopService(serviceIntent);

        AlarmReceiver alarmReceiver = new AlarmReceiver();
        alarmReceiver.setAlarm(context);
    }



    /**
     * Create first user, first fridge and first customList on first launch
     */
    private void loadDatabase() {
        User user = new User();
        user.setEmail("toto.albert@nowaste.fr");
        user.setFirstName("Toto");
        user.setLastName("Albert");
        /**
         * Throw UserCreatedEvent
         */
        userProvider.create(user);
    }

    public void onEvent(UserCreatedEvent event) {
        User user = event.getUser();
        addFridgeToUser(user);
    }

    private void addFridgeToUser(User user) {
        Fridge f = new Fridge();

        f.setName("Default fridge");
        f.setUser(user);
        /**
         * Throw FridgeCreatedEvent
         */
        fridgeProvider.create(f);
    }

    public void onEvent(FridgeCreatedEvent event) {
        User user = event.getFridge().getUser();
        addCustomListToUser(user);
    }

    private void addCustomListToUser(User user) {
        CustomList customList = new CustomList();

        customList.setName("Ma liste");
        customList.setUser(user);
        /**
         * Throw CustomListCreatedEvent
         */
        customListProvider.create(customList);
    }

    public void onEvent(CustomListCreatedEvent event) {
        User user = event.getCustomList().getUser();
        initApp(user);
        settings.edit().putLong("user", user.getId()).commit();
    }


    /**
     * Setup activity
     */
    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().registerSticky(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
