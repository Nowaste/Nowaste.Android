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

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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

import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

/**
 * Created by quentin on 24/06/15.
 */
public class NavigationDrawerFragment extends Fragment {

    private static String TAG = NavigationDrawerFragment.class.getSimpleName();

    private RecyclerView mRecyclerView;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private NavigationDrawerAdapter mAdapter;
    private View mContainerView;
    private static List<NavigationDrawerItem> mMenuItems = new ArrayList<NavigationDrawerItem>();
    private FragmentDrawerListener mDrawerListener;
    private User user;

    public NavigationDrawerFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflating view layout
        View layout = inflater.inflate(R.layout.nav_drawer_fragment, container, false);
        mRecyclerView = ButterKnife.findById(layout, R.id.drawerList);
        //mRecyclerView = (RecyclerView) layout.findViewById(R.id.drawerList);

        mAdapter = new NavigationDrawerAdapter();

        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), mRecyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                mDrawerListener.onDrawerItemSelected(view, position, mAdapter.get(position));
                mDrawerLayout.closeDrawer(mContainerView);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        return layout;
    }

    public void setUp(User user, int fragmentId, DrawerLayout drawerLayout, final Toolbar toolbar) {
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

        mContainerView = getActivity().findViewById(fragmentId);
        mDrawerLayout = drawerLayout;
        mDrawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout,
                toolbar, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                toolbar.setAlpha(1 - slideOffset / 2);
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

    public void setDrawerListener(FragmentDrawerListener listener) {
        mDrawerListener = listener;
    }

    interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }


    class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        }
    }

    public interface FragmentDrawerListener {
        public void onDrawerItemSelected(View view, int position, NavigationDrawerItem menuItem);
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
                FridgeFragment.newInstance(fridge),
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
                CustomListFragment.newInstance(customList),
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
        //mAdapter.remove(item);
    }

    public void onEvent(CustomListDeletedEvent event) {
        //mAdapter.remove(item);
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
