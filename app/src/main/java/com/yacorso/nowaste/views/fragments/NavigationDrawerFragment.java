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

package com.yacorso.nowaste.views.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yacorso.nowaste.R;
import com.yacorso.nowaste.events.CustomListCreatedEvent;
import com.yacorso.nowaste.events.CustomListDeletedEvent;
import com.yacorso.nowaste.events.CustomListUpdatedEvent;
import com.yacorso.nowaste.events.FridgeCreatedEvent;
import com.yacorso.nowaste.events.FridgeDeletedEvent;
import com.yacorso.nowaste.events.FridgeUpdatedEvent;
import com.yacorso.nowaste.models.CustomList;
import com.yacorso.nowaste.models.FoodList;
import com.yacorso.nowaste.models.Fridge;
import com.yacorso.nowaste.models.NavigationDrawerItem;
import com.yacorso.nowaste.models.User;
import com.yacorso.nowaste.views.adapters.NavigationDrawerAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.*;
import de.greenrobot.event.EventBus;

public class NavigationDrawerFragment extends Fragment {

    private static String TAG = NavigationDrawerFragment.class.getSimpleName();

    @Bind(R.id.drawerList) RecyclerView mRecyclerView;
    private NavigationDrawerAdapter mAdapter;
    private static List<NavigationDrawerItem> mMenuItems = new ArrayList<>();

    public NavigationDrawerFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflating view layout
        View layout = inflater.inflate(R.layout.nav_drawer_fragment, container, false);
        ButterKnife.bind(this, layout);

        mAdapter = new NavigationDrawerAdapter();

        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return layout;
    }

    public void setUp(User user) {
        List<Fridge> fridges = user.getFridges();

        for (Fridge fridge : fridges) {
            addFridgeToMenu(fridge);
        }

        List<CustomList> customLists = user.getCustomLists();

        for (CustomList customList : customLists) {
            addCustomListToMenu(customList);
        }

        addSettingsToMenu();
        displayNavigationDrawerList();
    }

    private void displayNavigationDrawerList() {
        // Update the adapter and notify data set changed
        mAdapter.clear();
        mAdapter.addAll(mMenuItems);
    }

    private NavigationDrawerItem addFridgeToMenu (Fridge fridge) {
        NavigationDrawerItem item = new NavigationDrawerItem(
                fridge.getName(),
                R.drawable.ic_fridge,
                FoodListFragment.newInstance(fridge),
                fridge.getId(),
                fridge.getClass()
        );

        mMenuItems.add(item);
        return item;
    }

    private NavigationDrawerItem addCustomListToMenu (CustomList customList) {
        NavigationDrawerItem item = new NavigationDrawerItem(
                customList.getName(),
                R.drawable.ic_folder,
                FoodListFragment.newInstance(customList),
                customList.getId(),
                customList.getClass()
        );

        mMenuItems.add(item);
        return item;
    }

    private NavigationDrawerItem addSettingsToMenu () {
        NavigationDrawerItem menuItemSettings = new NavigationDrawerItem(
                getResources().getString(R.string.menu_title_settings),
                R.drawable.ic_setting_light,
                SettingsFragment.newInstance(),
                0,
                null
        );

        mMenuItems.add(menuItemSettings);
        return menuItemSettings;
    }

    private NavigationDrawerItem updateMenu (FoodList foodList) {
        for (NavigationDrawerItem item : mMenuItems) {
            if (item.getId() == foodList.getId() && item.getType() == foodList.getClass()) {
                item.setTitle(foodList.getName());
                return item;
            }
        }
        return null;
    }

    public void onEvent(FridgeCreatedEvent event) {
        NavigationDrawerItem item = addFridgeToMenu(event.getFridge());
        mAdapter.add(item);
    }

    public void onEvent(CustomListCreatedEvent event) {
        NavigationDrawerItem item = addCustomListToMenu(event.getCustomList());
        mAdapter.add(item);
    }

    public void onEvent(FridgeUpdatedEvent event) {
        NavigationDrawerItem item = updateMenu(event.getFridge());
        if (item != null) {
            mAdapter.updateItemAt(mAdapter.indexOf(item), item);
        }
    }

    public void onEvent(CustomListUpdatedEvent event) {
        NavigationDrawerItem item = updateMenu(event.getCustomList());
        if (item != null) {
            mAdapter.updateItemAt(mAdapter.indexOf(item), item);
        }
    }

    public void onEvent(FridgeDeletedEvent event) {
        //adapter.remove(item);
    }

    public void onEvent(CustomListDeletedEvent event) {
        //adapter.remove(item);
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
}
