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


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import com.yacorso.nowaste.R;
import com.yacorso.nowaste.events.CallAddFoodEvent;
import com.yacorso.nowaste.events.CancelSearchEvent;
import com.yacorso.nowaste.events.CurrentFridgeChangedEvent;
import com.yacorso.nowaste.events.FoodCreatedEvent;
import com.yacorso.nowaste.events.FoodUpdatedEvent;
import com.yacorso.nowaste.events.FridgesLoadedEvent;
import com.yacorso.nowaste.events.LaunchSearchEvent;
import com.yacorso.nowaste.models.Food;
import com.yacorso.nowaste.models.FoodList;
import com.yacorso.nowaste.models.Fridge;
import com.yacorso.nowaste.providers.FridgeProvider;
import com.yacorso.nowaste.utils.LogUtil;
import com.yacorso.nowaste.views.adapters.FoodListAdapter;

import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

/**
 * A placeholder fragment containing a simple view.
 */
public class FoodListFragment extends BaseFragment {

    RecyclerView mRecyclerView;
    LinearLayoutManager mLayoutManager;
    FloatingActionButton mFabButton;
    SwipeRefreshLayout mSwipeRefreshLayout;
    FoodListAdapter mAdapter;
    FoodList mFoodList;
    FridgeProvider mFridgeProvider;

    public static FoodListFragment newInstance() {
        return new FoodListFragment();
    }

    public static FoodListFragment newInstance(FoodList foodList) {

        FoodListFragment fragment = new FoodListFragment();
        fragment.mFoodList = foodList;

        return fragment;
    }

    public FoodListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = super.onCreateView(inflater, container, savedInstanceState);

        mFridgeProvider = new FridgeProvider();

        setList();

        return mRootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        // Registring the bus for MessageEvent
        EventBus.getDefault().register(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        LogUtil.LOGD(this, "OnResume");

//        EventBus.getDefault().post(new LoadFoodsEvent(new Fridge()));
    }

    @Override
    public void onStop() {
        // Unregistering the bus
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    public void onEvent(FridgesLoadedEvent event) {
        LogUtil.LOGD(this, "onFridgeLoaded");
        LogUtil.LOGD(this, event.getFridges().toString());
    }

    public void onEvent(CurrentFridgeChangedEvent event) {
        mFoodList = event.getFridge();
        displayFoodList();
    }

    public void onEvent(FoodCreatedEvent event) {
        Food food = event.getFood();
        addFood(food);
    }

    private void addFood (Food food) {
        mAdapter.add(food);
        mFoodList.addFood(food);
        mFridgeProvider.update((Fridge) mFoodList);
    }

    public void onEvent(FoodUpdatedEvent event) {
        Food food = event.getFood();
        updateFood(food);
    }

    private void updateFood (Food food) {
        int test = mAdapter.indexOf(food);
        mAdapter.updateItemAt(mAdapter.indexOf(food), food);
        mFoodList.updateFood(food);
        mFridgeProvider.update((Fridge)mFoodList);
    }

    private void removeFood(Food food) {
        mAdapter.remove(food);
        mFoodList.removeFood(food);
        mFridgeProvider.update((Fridge)mFoodList);
    }

    public void onEvent(LaunchSearchEvent event) {
        String search = event.getSearchQuery();
        mAdapter.setFilter(search, mFoodList.getFoods());
    }

    public void onEvent(CancelSearchEvent event) {
        displayFoodList();
    }


    private void setList() {
        initSwipeRefreshLayout();
        initRecyclerView();
        initFabButton();

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new FoodListAdapter();
        mRecyclerView.setAdapter(mAdapter);

        displayFoodList();
    }

    private void initSwipeRefreshLayout() {
        mSwipeRefreshLayout = ButterKnife.findById(mRootView, R.id.swipeRefreshFoodListLayout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.refresh_progress_1, R.color.refresh_progress_2);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                displayFoodList();
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

                Snackbar.make(getView(), R.string.snackbar_confirm_food_delete, Snackbar.LENGTH_LONG)
                        .setAction(R.string.snackbar_undo, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                addFood(item);
                            }
                        })
                        .show();

                // callback for swipe to dismiss, removing item from data and adapter
                removeFood(item);
            }
        });

        swipeToDismissTouchHelper.attachToRecyclerView(mRecyclerView);

        /**
         * Si on veut cacher la toolbar et le bouton lors du scroll
         * Déjà implété avec ces fonctions
         */
//        recyclerView.clearOnScrollListeners();
//        recyclerView.addOnScrollListener(new HidingScrollListener() {
//            @Override
//            public void onHide() {
////                hideViews();
//            }
//
//            @Override
//            public void onShow() {
////                showViews();
//            }
//        });
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

    private void hideViews() {

        CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) mFabButton.getLayoutParams();
        int fabBottomMargin = lp.bottomMargin;
        mFabButton.animate().translationY(mFabButton.getHeight() + fabBottomMargin)
                .setInterpolator(new AccelerateInterpolator(2)).start();
    }

    private void showViews() {
        mFabButton.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();
    }

    private void displayFoodList() {
        // Update the adapter and notify data set changed
        mAdapter.clear();
        mAdapter.addAll(mFoodList.getFoods());

        // Stop refresh animation
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public int getTitle() {
        return R.string.app_name;
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_food_list;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        LogUtil.LOGD(this,"onCreateOptionsMenu");
        super.onCreateOptionsMenu(menu, inflater);
    }
}
