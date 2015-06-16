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
import com.yacorso.nowaste.R;
import com.yacorso.nowaste.model.Food;
import com.yacorso.nowaste.view.adapter.FoodListAdapter;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.List;

import butterknife.ButterKnife;

/**
 * A placeholder fragment containing a simple view.
 */
public class FoodListFragment extends BaseFragment {

    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    FoodListAdapter foodListAdapter;
    FloatingActionButton mFabButton;
    SwipeRefreshLayout mSwipeRefreshLayout;
    List<Food> foodItems;


    public static FoodListFragment newInstance() {
        return new FoodListFragment();
    }

    public FoodListFragment() {
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setList();
    }

    private void setList() {
        initSwipeRefreshLayout();
        initRecyclerView();
        initFabButton();

        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        foodListAdapter = new FoodListAdapter();
        recyclerView.setAdapter(foodListAdapter);

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
        recyclerView = ButterKnife.findById(getActivity(), R.id.rv_food_list);
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

    private void initFabButton(){
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
        mFabButton.animate().translationY(mFabButton.getHeight()+fabBottomMargin)
                .setInterpolator(new AccelerateInterpolator(2)).start();
    }

    private void showViews() {
        mToolbar.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2));
        mFabButton.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();
    }

    private void refreshItems() {

        // Load items
        foodItems = new Select().from(Food.class).queryList();

        // Load complete
        onItemsLoadComplete();
    }

    private void onItemsLoadComplete() {
        // Update the adapter and notify data set changed
        foodListAdapter.setFoods(foodItems);
        foodListAdapter.notifyDataSetChanged();

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
        SecureRandom random = new SecureRandom();
        Food food = new Food();
        food.setName(new BigInteger(32, random).toString());

        food.save();
    }

}
