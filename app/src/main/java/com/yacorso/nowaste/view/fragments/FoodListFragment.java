package com.yacorso.nowaste.view.fragments;

import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.raizlabs.android.dbflow.sql.language.Select;
import com.yacorso.nowaste.R;
import com.yacorso.nowaste.model.Food;
import com.yacorso.nowaste.view.adapter.FoodAdapter;

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
    FoodAdapter foodAdapter;
    FloatingActionButton btnAdd;
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
        mSwipeRefreshLayout = ButterKnife.findById(getActivity(), R.id.swipeRefreshFoodListLayout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){
            @Override
            public void onRefresh() {
                refreshItems();
            }
        });
        mSwipeRefreshLayout.setColorSchemeColors(R.color.accent_material_light);

        recyclerView = ButterKnife.findById(getActivity(), R.id.rv_food_list);

        layoutManager = new LinearLayoutManager(getActivity());

        recyclerView.setLayoutManager(layoutManager);

        foodAdapter = new FoodAdapter();
        recyclerView.setAdapter(foodAdapter);

        refreshItems();

        FloatingActionButton myFab = ButterKnife.findById(getDrawerActivity(), R.id.btnAddFood);
        myFab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                addFood();
            }
        });
    }

    private void refreshItems(){

        // Load items
        foodItems = new Select().from(Food.class).queryList();

        // Load complete
        onItemsLoadComplete();
    }

    private void onItemsLoadComplete() {
        // Update the adapter and notify data set changed
        foodAdapter.setFoods(foodItems);
        foodAdapter.notifyDataSetChanged();

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

    public void addFood(){
        SecureRandom random = new SecureRandom();
        Food food = new Food();
        food.setName(new BigInteger(32,random).toString());

        food.save();
    }

}
