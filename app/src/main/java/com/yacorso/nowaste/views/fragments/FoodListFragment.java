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
import com.yacorso.nowaste.events.CallSetFoodEvent;
import com.yacorso.nowaste.events.CallSpeechAddFoodEvent;
import com.yacorso.nowaste.events.CancelSearchEvent;
import com.yacorso.nowaste.events.FoodCreatedEvent;
import com.yacorso.nowaste.events.FoodDeletedEvent;
import com.yacorso.nowaste.events.FoodUpdatedEvent;
import com.yacorso.nowaste.events.FridgesLoadedEvent;
import com.yacorso.nowaste.events.LaunchSearchEvent;
import com.yacorso.nowaste.models.CustomList;
import com.yacorso.nowaste.models.Food;
import com.yacorso.nowaste.models.FoodList;
import com.yacorso.nowaste.models.Fridge;
import com.yacorso.nowaste.providers.CustomListProvider;
import com.yacorso.nowaste.providers.FoodProvider;
import com.yacorso.nowaste.providers.FridgeProvider;
import com.yacorso.nowaste.utils.LogUtil;
import com.yacorso.nowaste.views.adapters.BaseAdapter;
import com.yacorso.nowaste.views.adapters.CustomListFoodAdapter;
import com.yacorso.nowaste.views.adapters.FridgeFoodAdapter;

import butterknife.Bind;
import de.greenrobot.event.EventBus;

/**
 * A placeholder fragment containing a simple view.
 */
public class FoodListFragment extends BaseFragment {

    @Bind(R.id.rv_food_list)RecyclerView mRecyclerView;
    LinearLayoutManager mLayoutManager;
    @Bind(R.id.btnAddFood) FloatingActionButton mFabButton;
    @Bind(R.id.swipeRefreshFoodListLayout) SwipeRefreshLayout mSwipeRefreshLayout;
    BaseAdapter adapter;
    FoodList foodList;
    FridgeProvider fridgeProvider;
    CustomListProvider customListProvider;
    FoodProvider foodProvider;

    public static FoodListFragment newInstance(FoodList foodList) {

        FoodListFragment fragment = new FoodListFragment();
        fragment.foodList = foodList;

        if(fragment.foodList.getClass().equals(CustomList.class)){
            fragment.adapter = new CustomListFoodAdapter();
        }else{
            fragment.adapter = new FridgeFoodAdapter();
        }

        EventBus.getDefault().register(fragment);

        return fragment;
    }

    public FoodListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mRootView = super.onCreateView(inflater, container, savedInstanceState);

        fridgeProvider = new FridgeProvider();
        customListProvider = new CustomListProvider();
        foodProvider = new FoodProvider();

        setList();

        return mRootView;
    }

    private void setList() {
        initSwipeRefreshLayout();
        initRecyclerView();
        initFabButton();

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);


        if(adapter == null){
            if(foodList.getClass().equals(CustomList.class)){
                adapter = new CustomListFoodAdapter();
            }else{
                adapter = new FridgeFoodAdapter();
            }
        }

        mRecyclerView.setAdapter(adapter);

        displayFoodList();
    }

    private void initSwipeRefreshLayout() {
        mSwipeRefreshLayout.setColorSchemeResources(R.color.refresh_progress_1, R.color.refresh_progress_2);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                EventBus.getDefault().post(new CancelSearchEvent());
            }
        });
    }

    private void initRecyclerView() {

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
                final Food item = adapter.get(position);
                adapter.remove(item);

                final boolean[] hasCancel = {false};
                Snackbar snack = Snackbar.make(getView(), R.string.snackbar_confirm_food_delete, Snackbar.LENGTH_LONG)
                                         .setAction(R.string.snackbar_undo, new View.OnClickListener() {
                                             @Override
                                             public void onClick(View v) {
                                                 hasCancel[0] = true;
                                                 adapter.add(item);
                                             }
                                         });

                snack.getView().addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
                    @Override
                    public void onViewAttachedToWindow(View v) { }

                    @Override
                    public void onViewDetachedFromWindow(View v) {
                        if (!hasCancel[0]) {
                            foodProvider.delete(item);
                        }
                    }
                });
                snack.show();
            }
        });

        swipeToDismissTouchHelper.attachToRecyclerView(mRecyclerView);

        /**
         * Si on veut cacher la toolbar et le bouton lors du scroll
         * Déjà implémenté avec ces fonctions
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
        mFabButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //EventBus.getDefault().post(new CallSetFoodEvent(null));
                EventBus.getDefault().post(new CallSpeechAddFoodEvent());
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
        adapter.clear();
        adapter.addAll(foodList.getFoods());

        // Stop refresh animation
        if(mSwipeRefreshLayout != null){
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }


    public void onEvent(FridgesLoadedEvent event) {
        LogUtil.LOGD(this, "onFridgeLoaded");
        LogUtil.LOGD(this, event.getFridges().toString());
    }

    public void onEvent(FoodCreatedEvent event) {
        LogUtil.LOGD(this,"FoodCreatedEvent");
        Food food = event.getFood();

        if((food.getFridge() != null && foodList.getClass().equals(Fridge.class)
                && food.getFridge().getId() == foodList.getId()) ||
            (food.getCustomList() != null && foodList.getClass().equals(CustomList.class)
                    && food.getCustomList().getId() == foodList.getId())){
            foodList.addFood(food);
            adapter.add(food);
        }

    }

    public void onEvent(FoodUpdatedEvent event) {
        Food food = event.getFood();
        adapter.updateItem(food);
    }

    public void onEvent(FoodDeletedEvent event) {
        LogUtil.LOGD(this,"FoodDeletedEvent");
        Food food = event.getFood();
        foodList.removeFood(food);
    }

    public void onEvent(LaunchSearchEvent event) {
        String search = event.getSearchQuery();
        adapter.setFilter(search, foodList.getFoods());
    }

    public void onEvent(CancelSearchEvent event) {
        displayFoodList();
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
        LogUtil.LOGD(this, "onCreateOptionsMenu");
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Registring the bus for MessageEvent
//        EventBus.getDefault().register(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        LogUtil.LOGD(this, "OnResume");

        if(! EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }


//        EventBus.getDefault().post(new LoadFoodsEvent(new Fridge()));
    }

    @Override
    public void onStop() {
        LogUtil.LOGD(this, "OnStop");

        // Unregistering the bus
//        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    public FoodList getFoodList() {
        return foodList;
    }
}
