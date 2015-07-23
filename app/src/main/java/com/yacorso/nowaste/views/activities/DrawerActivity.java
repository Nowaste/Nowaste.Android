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
import android.content.res.Configuration;
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
import com.yacorso.nowaste.events.CallAddFoodListEvent;
import com.yacorso.nowaste.events.CallAddFoodToCustomListEvent;
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
import com.yacorso.nowaste.utils.Utils;
import com.yacorso.nowaste.views.fragments.AddFoodListDialog;
import com.yacorso.nowaste.views.fragments.AddFoodToCustomListDialog;
import com.yacorso.nowaste.views.fragments.ChooseTypeFoodListDialog;
import com.yacorso.nowaste.views.fragments.LoginFragment;
import com.yacorso.nowaste.views.fragments.SetFoodToFoodListDialog;
import com.yacorso.nowaste.views.fragments.BaseFragment;
import com.yacorso.nowaste.views.fragments.FoodListFragment;
import com.yacorso.nowaste.views.fragments.SettingsFragment;
import com.yacorso.nowaste.views.fragments.SpeechAddFoodFragment;
import com.yacorso.nowaste.views.old.MainActivity;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import butterknife.*;
import de.greenrobot.event.EventBus;

public class DrawerActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    @Bind(R.id.drawer_layout) DrawerLayout mDrawerLayout;
    @Bind(R.id.main_navigation) NavigationView navigationView;
    ActionBarDrawerToggle mDrawerToggle;
    NavigatorUtil mNavigatorUtil;
    @Bind(R.id.toolbar) Toolbar mToolbar;

    private MenuItem mSearchAction;
    FoodList currentFoodList;
    User currentUser;
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


        settings = getSharedPreferences("preferences", MODE_PRIVATE);
        long userId = settings.getLong("user", 0);

        if(userId == 0){
            /**
             * Load database and call initApp after it is done
             */
            loadDatabase();
        }
        else{
            initApp(userProvider.get(userId));
            loadNavItems();
            changeRootFragment(0, true);
        }
    }

    public void initApp(User user) {
        currentUser = user;

        initToolbar();
        loadNavDrawer();
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

    private void updateToolbarTitle(int titleId) {
        updateToolbarTitle(getString(titleId));
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
    private void loadNavDrawer () {
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
    }

    private void loadNavItems() {
        int index = 0;
        Menu navigationMenu = navigationView.getMenu();
        navigationMenu.clear();
        navigationItems.clear();

        SubMenu fridgeSection = navigationMenu.addSubMenu(getText(R.string.menu_section_fridges));
        List<Fridge> fridges = currentUser.getFridges();
        for (Fridge fridge : fridges) {
            navigationItems.put(index, fridge);
            fridgeSection.add(0, index++, 0, fridge.getName()).setIcon(R.drawable.ic_fridge);
        }

        SubMenu customListSection = navigationMenu.addSubMenu(getText(R.string.menu_section_custom_lists));
        List<CustomList> customLists = currentUser.getCustomLists();

        for (CustomList customList : customLists) {
            navigationItems.put(index, customList);
            customListSection.add(1, index++, 0, customList.getName()).setIcon(R.drawable.ic_folder);
        }

        navigationMenu.add(2, index++, 0, R.string.menu_add_foodlist).setIcon(R.drawable.ic_add_circle_white);
        navigationMenu.add(3, index, 0, R.string.menu_title_settings).setIcon(R.drawable.ic_setting_dark);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                if (menuItem.getGroupId() < 2) {
                    changeRootFragment(menuItem.getItemId(), true);
                } else if (menuItem.getGroupId() == 2) {
                    launchDialog(ChooseTypeFoodListDialog.newInstance());
                } else if (menuItem.getGroupId() == 3) {
                    changeRootFragment(menuItem.getItemId(), false);
                }

                return true;
            }
        });

        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });
    }

    @OnClick(R.id.profile_image)
    void login() {
        LoginFragment loginFragment = LoginFragment.newInstance();
        addFragment(loginFragment);
    }



    /**
     * Events to change fragment/dialog displayed
     */
    private void changeRootFragment(int id, boolean isFoodList) {
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

        changeRootFragment(fragment, title);
    }

    private void changeRootFragment(BaseFragment fragment, String title) {
        mNavigatorUtil.setRootFragment(fragment);
        mDrawerLayout.closeDrawer(navigationView);
        updateToolbarTitle(title);
    }

    private void addFragment(BaseFragment fragment) {
        mNavigatorUtil.goTo(fragment);
        mDrawerLayout.closeDrawer(navigationView);
        updateToolbarTitle(getString(fragment.getTitle()));
        mDrawerToggle.setDrawerIndicatorEnabled(false);
    }

    public void onEvent(CallSpeechAddFoodEvent event) {
        launchDialog(SpeechAddFoodFragment.newInstance());
    }

    public void onEvent(CallCreateFoodEvent event) {
        launchDialog(SetFoodToFoodListDialog.newInstance(event.getFood(), currentFoodList, Utils.TYPE_CREATE));
    }

    public void onEvent(CallUpdateFoodEvent event) {
        launchDialog(SetFoodToFoodListDialog.newInstance(event.getFood(), currentFoodList, Utils.TYPE_UPDATE));
    }

    public void onEvent(SpeechFoodMatchEvent event) {
        launchDialog(SetFoodToFoodListDialog.newInstance(event.getFood(), currentFoodList, Utils.TYPE_CREATE));
    }

    public void onEvent(CallAddFoodToCustomListEvent event) {
        launchDialog(AddFoodToCustomListDialog.newInstance(event.getFood()));
    }

    public void onEvent(CallAddFoodListEvent event) {
        FoodList foodList;
        if (event.getType() == Utils.TYPE_FRIDGE) {
            foodList = new Fridge();
        }
        else {
            foodList = new CustomList();
        }

        foodList.setUser(currentUser);
        launchDialog(AddFoodListDialog.newInstance(foodList));
    }

    public void launchDialog(BaseFragment fragment) {
        fragment.show(getSupportFragmentManager(), "dialog");
    }

    public void onEvent(FridgeCreatedEvent event) {
        Fridge fridge = event.getFridge();

        BaseFragment fragment = FoodListFragment.newInstance(fridge);
        String title = fridge.getName();
        changeRootFragment(fragment, title);

        loadNavItems();

        currentFoodList = fridge;
    }

    public void onEvent(CustomListCreatedEvent event) {
        CustomList customList = event.getCustomList();

        BaseFragment fragment = FoodListFragment.newInstance(customList);
        String title = customList.getName();
        changeRootFragment(fragment, title);

        loadNavItems();

        currentFoodList = customList;
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
        initApp(user);
        addCustomListToUser(user);
        addFridgeToUser(user);
        settings.edit().putLong("user", user.getId()).apply();
    }

    private void addFridgeToUser(User user) {
        Fridge f = new Fridge();

        f.setName(getString(R.string.menu_title_my_fridge));
        f.setUser(user);
        /**
         * Throw FridgeCreatedEvent
         */
        fridgeProvider.create(f);
    }

    private void addCustomListToUser(User user) {
        CustomList customList = new CustomList();

        customList.setName(getString(R.string.menu_title_my_list));
        customList.setUser(user);
        /**
         * Throw CustomListCreatedEvent
         */
        customListProvider.create(customList);
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
        mDrawerToggle.setDrawerIndicatorEnabled(true);
        updateToolbarTitle(currentFoodList.getName());
    }
}
