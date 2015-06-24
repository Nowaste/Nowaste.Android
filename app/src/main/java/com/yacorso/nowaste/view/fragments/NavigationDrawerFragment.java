package com.yacorso.nowaste.view.fragments;

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

import com.yacorso.nowaste.NowasteApplication;
import com.yacorso.nowaste.R;
import com.yacorso.nowaste.events.UserLoadedEvent;
import com.yacorso.nowaste.models.Fridge;
import com.yacorso.nowaste.models.NavigationDrawerItem;
import com.yacorso.nowaste.models.User;
import com.yacorso.nowaste.view.adapter.NavigationDrawerAdapter;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by quentin on 24/06/15.
 */
public class NavigationDrawerFragment extends Fragment{

    private static String TAG = NavigationDrawerFragment.class.getSimpleName();

    private RecyclerView mRecyclerView;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private NavigationDrawerAdapter mAdapter;
    private View mContainerView;
    private static List<String> mTitles = new ArrayList<String>();
    private FragmentDrawerListener mDrawerListener;

    public NavigationDrawerFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflating view layout
        View layout = inflater.inflate(R.layout.nav_drawer_fragment, container, false);
        mRecyclerView = (RecyclerView) layout.findViewById(R.id.drawerList);

        User currentUser = NowasteApplication.getCurrentUser();
        List<Fridge> fridges = currentUser.getFridges();

        for (Fridge fridge: fridges){
            mTitles.add(fridge.getName());
        }

        mAdapter = new NavigationDrawerAdapter(getData());

        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), mRecyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                mDrawerListener.onDrawerItemSelected(view, position);
                mDrawerLayout.closeDrawer(mContainerView);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        return layout;
    }

    public void setUp(int fragmentId, DrawerLayout drawerLayout, final Toolbar toolbar) {
        mContainerView = getActivity().findViewById(fragmentId);
        mDrawerLayout = drawerLayout;
        mDrawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {
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

    public static List<NavigationDrawerItem> getData() {
        List<NavigationDrawerItem> data = new ArrayList<>();


        // preparing navigation drawer items
        for (int i = 0; i < mTitles.size(); i++) {
            NavigationDrawerItem navItem = new NavigationDrawerItem();
            navItem.setTitle(mTitles.get(i));
            data.add(navItem);
        }
        return data;
    }


    public void setDrawerListener(FragmentDrawerListener listener) {
        mDrawerListener = listener;
    }

    public static interface ClickListener {
        public void onClick(View view, int position);

        public void onLongClick(View view, int position);
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
        public void onDrawerItemSelected(View view, int position);
    }
}
