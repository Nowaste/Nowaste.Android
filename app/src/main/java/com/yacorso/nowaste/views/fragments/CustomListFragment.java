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
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yacorso.nowaste.R;
import com.yacorso.nowaste.events.CallAddFoodEvent;
import com.yacorso.nowaste.events.CancelSearchEvent;
import com.yacorso.nowaste.models.Food;
import com.yacorso.nowaste.models.FoodList;
import com.yacorso.nowaste.providers.CustomListProvider;
import com.yacorso.nowaste.providers.FoodProvider;
import com.yacorso.nowaste.providers.FridgeProvider;
import com.yacorso.nowaste.providers.UserProvider;
import com.yacorso.nowaste.utils.LogUtil;
import com.yacorso.nowaste.views.adapters.CustomListFoodAdapter;
import com.yacorso.nowaste.views.adapters.FridgeFoodAdapter;

import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

public class CustomListFragment extends BaseFragment {
    RecyclerView mRecyclerView;
    LinearLayoutManager mLayoutManager;
    FloatingActionButton mFabButton;
    SwipeRefreshLayout mSwipeRefreshLayout;
    CustomListFoodAdapter mAdapter;
    FoodList mFoodList;
    CustomListProvider mCustomListProvider;
    FoodProvider mFoodProvider;

    public static CustomListFragment newInstance() {
        return new CustomListFragment();
    }

    public static CustomListFragment newInstance(FoodList foodList) {

        CustomListFragment fragment = new CustomListFragment();
        fragment.mFoodList = foodList;

        return fragment;
    }

    public CustomListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = super.onCreateView(inflater, container, savedInstanceState);

        mCustomListProvider = new CustomListProvider();
        mFoodProvider = new FoodProvider();

        setList();

        return mRootView;
    }

    private void setList() {
        initSwipeRefreshLayout();
        initRecyclerView();
        initFabButton();

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new CustomListFoodAdapter();
        mRecyclerView.setAdapter(mAdapter);

        displayFoodList();
    }

    private void initSwipeRefreshLayout() {
        mSwipeRefreshLayout = ButterKnife.findById(mRootView, R.id.swipeRefreshFoodListLayout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.refresh_progress_1, R.color.refresh_progress_2);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                EventBus.getDefault().post(new CancelSearchEvent());
            }
        });
    }

    private void initRecyclerView() {
        mRecyclerView = ButterKnife.findById(mRootView, R.id.rv_food_list);


        ItemTouchHelper swipeToDismissTouchHelper = new ItemTouchHelper(
                new ItemTouchHelper.SimpleCallback( ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT,
                        ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
                    @Override
                    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                        // callback for drag-n-drop, false to skip this feature
                        return false;
                    }

                    @Override
                    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

                        final int position = viewHolder.getAdapterPosition();
                        final Food item = mAdapter.get(position);
                        mAdapter.remove(item);

                        final boolean[] hasCancel = {false};
                        Snackbar snack = Snackbar.make(getView(), R.string.snackbar_confirm_food_delete, Snackbar.LENGTH_LONG)
                                .setAction(R.string.snackbar_undo, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        hasCancel[0] = true;
                                        mAdapter.add(item);
                                    }
                                });

                        snack.getView().addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
                            @Override
                            public void onViewAttachedToWindow(View v) { }

                            @Override
                            public void onViewDetachedFromWindow(View v) {
                                if (!hasCancel[0]) {
                                    mFoodProvider.delete(item);
                                }
                            }
                        });
                        snack.show();
                    }
                });

        swipeToDismissTouchHelper.attachToRecyclerView(mRecyclerView);

    }

    private void initFabButton() {
        mFabButton = ButterKnife.findById(mRootView, R.id.btnAddFood);
        mFabButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                EventBus.getDefault().post(new CallAddFoodEvent());
//                EventBus.getDefault().post(new AddFoodEvent());
//                mSpeechRecognizer.stopListening();
//                SpeechAddFoodFragment fragment = SpeechAddFoodFragment.newInstance();
//                fragment.show(getSupportFragmentManager(), "dialog");
            }
        });
    }

    private void displayFoodList() {
        // Update the adapter and notify data set changed
        mAdapter.clear();
        mAdapter.addAll(mFoodList.getFoods());

        // Stop refresh animation
        mSwipeRefreshLayout.setRefreshing(false);
    }


    public void onEvent(CancelSearchEvent event) {
        displayFoodList();
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_food_list;
    }

    public void onStart() {
        super.onStart();
        // Registring the bus for MessageEvent
        EventBus.getDefault().register(this);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStop() {
        // Unregistering the bus
        EventBus.getDefault().unregister(this);
        super.onStop();
    }
}
