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
import android.support.v4.app.FragmentActivity;
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

import com.yacorso.nowaste.NowasteApplication;
import com.yacorso.nowaste.R;
import com.yacorso.nowaste.events.AddFoodEvent;
import com.yacorso.nowaste.events.CancelSearchEvent;
import com.yacorso.nowaste.events.CurrentFridgeChangedEvent;
import com.yacorso.nowaste.events.FoodCreatedEvent;
import com.yacorso.nowaste.events.FridgesLoadedEvent;
import com.yacorso.nowaste.events.LaunchSearchEvent;
import com.yacorso.nowaste.models.CustomList;
import com.yacorso.nowaste.models.Food;
import com.yacorso.nowaste.models.FoodFridge;
import com.yacorso.nowaste.models.FoodList;
import com.yacorso.nowaste.models.Fridge;
import com.yacorso.nowaste.models.User;
import com.yacorso.nowaste.providers.FoodProvider;
import com.yacorso.nowaste.providers.FridgeProvider;
import com.yacorso.nowaste.utils.LogUtil;
import com.yacorso.nowaste.views.adapters.FoodListAdapter;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

/**
 * A placeholder fragment containing a simple view.
 */
public class FoodListFragment extends BaseFragment {

    RecyclerView mRecyclerView;
    LinearLayoutManager mLayoutManager;
    FoodListAdapter mFoodListAdapter;
    FloatingActionButton mFabButton;
    SwipeRefreshLayout mSwipeRefreshLayout;
    Fridge mCurrentFridge;
    CustomList mCurrentCustomList;
    List<Food> mFoodItems = new ArrayList<Food>();

    FoodProvider mFoodProvider;
    FridgeProvider mFridgeProvider;

    public static FoodListFragment newInstance() {
        return new FoodListFragment();
    }
    public static FoodListFragment newInstance(Fridge fridge) {

        FoodListFragment fragment = new FoodListFragment();
        fragment.mCurrentFridge = fridge;

        return fragment;
    }
    public static FoodListFragment newInstance(CustomList customList) {

        FoodListFragment fragment = new FoodListFragment();
        fragment.mCurrentCustomList = customList;

        return fragment;
    }

    public FoodListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = super.onCreateView(inflater, container, savedInstanceState);

        mFoodProvider = new FoodProvider();
        mFridgeProvider = new FridgeProvider();

//        mCurrentFridge = mFridgeProvider.getCurrentFridge();

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
        mFoodItems = event.getFridge().getFoods();
        refreshItems();
    }

    public void onEvent(FoodCreatedEvent event) {
        refreshItems();
    }

    public void onEvent(LaunchSearchEvent event) {
        String search = event.getSearchQuery();
        mFoodListAdapter.setFilter(search);
    }

    public void onEvent(CancelSearchEvent event) {
        mFoodListAdapter.flushFilter();
    }


    private void setList() {
        initSwipeRefreshLayout();
        initRecyclerView();
        initFabButton();

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mFoodListAdapter = new FoodListAdapter();
        mRecyclerView.setAdapter(mFoodListAdapter);

        refreshItems();

    }

    private void initSwipeRefreshLayout() {
        Activity a = getActivity();
        mSwipeRefreshLayout = ButterKnife.findById(mRootView, R.id.swipeRefreshFoodListLayout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.refresh_progress_1, R.color.refresh_progress_2);
//        mSwipeRefreshLayout.setProgressViewOffset(false, 150, 200);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshItems();
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
                final Food item = mFoodItems.get(position);

                Snackbar.make(getView(), R.string.snackbar_confirm_food_delete, Snackbar.LENGTH_LONG)
                        .setAction(R.string.snackbar_undo, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mFoodItems.add(position, item);
                                mFoodListAdapter.notifyDataSetChanged();
                            }
                        })
                        .show();


                // callback for swipe to dismiss, removing item from data and adapter
//                mFoodService.delete(item);
                mFoodItems.remove(position);
                mFoodListAdapter.notifyItemRemoved(position);
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
                EventBus.getDefault().post(new AddFoodEvent());
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

    private void refreshItems() {
//
////        // Load items
////        mFoodItems = new Select().from(Food.class).queryList();
////
//        User user = NowasteApplication.getCurrentUser();
//        if (user.exists() && user.getFridges().size() > 0) {
//            Fridge f = user.getFridges().get(0);
//
////            mCurrentFridge = user.getFridges().get(0);
//            mFoodItems = mCurrentFridge.getFoods();
//        }
////
////        mFoodItems = mCurrentFridge.getFoodList();

        if(mCurrentFridge != null && !mCurrentFridge.isEmpty()){
            mFoodItems = mCurrentFridge.getFoods();
        }else if(mCurrentCustomList != null && !mCurrentCustomList.isEmpty()){
            mFoodItems = mCurrentCustomList.getFoods();
        }

//        // Load complete
        onItemsLoadComplete();


    }

    private void onItemsLoadComplete() {
        // Update the adapter and notify data set changed
        mFoodListAdapter.setFoods(mFoodItems);
        mFoodListAdapter.notifyDataSetChanged();

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

    public void addFood() {

        Food food = new Food();
        FoodFridge foodFridge = food.getFoodFridge();
        foodFridge.setOutOfDate(new Date());
        foodFridge.setQuantity(5);
        foodFridge.setOpen(false);

//        List<Fridge> fridgeList = new Select().from(Fridge.class).queryList();
//        List<FoodFridge> foodFridgeList = new Select().from(FoodFridge.class).queryList();
//        List<Food> foodList = new Select().from(Food.class).queryList();
        food.setFridge(mCurrentFridge);

        SecureRandom random = new SecureRandom();
        food.setName(new BigInteger(32, random).toString());

        mCurrentFridge.addFood(food);

        mFridgeProvider.update(mCurrentFridge);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        LogUtil.LOGD(this,"onCreateOptionsMenu");
        super.onCreateOptionsMenu(menu, inflater);
    }
}
