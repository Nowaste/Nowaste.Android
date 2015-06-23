package com.yacorso.nowaste.view.fragments;

import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import com.raizlabs.android.dbflow.sql.language.Select;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;
import com.yacorso.nowaste.NowasteApplication;
import com.yacorso.nowaste.R;
import com.yacorso.nowaste.bus.BusProvider;
import com.yacorso.nowaste.dao.FoodDao;
import com.yacorso.nowaste.events.CurrentFridgeChangedEvent;
import com.yacorso.nowaste.events.FoodCreatedEvent;
import com.yacorso.nowaste.events.FridgesLoadedEvent;
import com.yacorso.nowaste.events.LoadFoodsEvent;
import com.yacorso.nowaste.events.LoadFridgesEvent;
import com.yacorso.nowaste.models.Food;
import com.yacorso.nowaste.models.FoodFridge;
import com.yacorso.nowaste.models.Fridge;
import com.yacorso.nowaste.models.User;
import com.yacorso.nowaste.services.FoodService;
import com.yacorso.nowaste.services.FridgeService;
import com.yacorso.nowaste.utils.LogUtil;
import com.yacorso.nowaste.view.adapter.FoodListAdapter;
import com.yacorso.nowaste.webservice.NowasteApi;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.ButterKnife;

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
    List<Food> mFoodItems = new ArrayList<Food>();
    Bus mBus;


    FoodService mFoodService;
    FridgeService mFridgeService;


    public static FoodListFragment newInstance() {
        return new FoodListFragment();
    }

    public FoodListFragment() {
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mFoodService = new FoodService(getBus());
        mFridgeService = new FridgeService(getBus());

        mCurrentFridge = mFridgeService.getCurrentFridge();

        setList();
        LogUtil.LOGD(this, "onActivityCreated");

    }

    @Override
    public void onResume() {
        super.onResume();
        LogUtil.LOGD(this, "OnResume");

        getBus().register(this);
        getBus().post(new LoadFoodsEvent(new Fridge()));
    }

    @Override
    public void onPause() {
        super.onPause();

        getBus().unregister(this);
    }

    @Subscribe
    public void onFridgesLoaded(FridgesLoadedEvent event) {
        LogUtil.LOGD(this, "onFridgeLoaded");
        LogUtil.LOGD(this, event.getFridges().toString());
    }

    @Subscribe
    public void onCurrentFridgeChangedEvent(CurrentFridgeChangedEvent event) {
        mFoodItems = event.getFridge().getFoods();
        refreshItems();
    }

    @Subscribe
    public void onFoodCreated(FoodCreatedEvent event) {
        refreshItems();
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
        mSwipeRefreshLayout = ButterKnife.findById(getActivity(), R.id.swipeRefreshFoodListLayout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.refresh_progress_1, R.color.refresh_progress_2);
        mSwipeRefreshLayout.setProgressViewOffset(false, 150, 200);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshItems();
            }
        });
    }

    private void initRecyclerView() {
        mRecyclerView = ButterKnife.findById(getActivity(), R.id.rv_food_list);
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
        mFabButton = ButterKnife.findById(getDrawerActivity(), R.id.btnAddFood);
        mFabButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                addFood();
            }
        });
    }

    private void hideViews() {
        mToolbar.animate().translationY(-mToolbar.getHeight())
                .setInterpolator(new AccelerateInterpolator(2));

        CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) mFabButton.getLayoutParams();
        int fabBottomMargin = lp.bottomMargin;
        mFabButton.animate().translationY(mFabButton.getHeight() + fabBottomMargin)
                .setInterpolator(new AccelerateInterpolator(2)).start();
    }

    private void showViews() {
        mToolbar.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2));
        mFabButton.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();
    }

    private void refreshItems() {

//        // Load items
//        mFoodItems = new Select().from(Food.class).queryList();
//
        User user = NowasteApplication.getCurrentUser();
        if (user.exists() && user.getFridges().size() > 0) {
            Fridge f = user.getFridges().get(0);

//            mCurrentFridge = user.getFridges().get(0);
            mFoodItems = mCurrentFridge.getFoods();
        }
//
//        mFoodItems = mCurrentFridge.getFoodList();

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
    protected int getTitle() {
        return R.string.menu_title_my_fridge;
    }

    @Override
    public boolean hasCustomToolbar() {
        return true;
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

        List<Fridge> fridgeList = new Select().from(Fridge.class).queryList();
        List<FoodFridge> foodFridgeList = new Select().from(FoodFridge.class).queryList();
        List<Food> foodList = new Select().from(Food.class).queryList();
        food.setFridge(mCurrentFridge);

        SecureRandom random = new SecureRandom();
        food.setName(new BigInteger(32, random).toString());

        mCurrentFridge.addFood(food);

        mFridgeService.update(mCurrentFridge);
    }

    private Bus getBus() {
        if (mBus == null) {
            mBus = BusProvider.getInstance();
        }
        return mBus;
    }

    public void setBus(Bus bus) {
        mBus = bus;
    }
}
