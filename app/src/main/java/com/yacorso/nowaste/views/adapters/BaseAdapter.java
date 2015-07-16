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

package com.yacorso.nowaste.views.adapters;

import android.content.Context;
import android.support.v7.util.SortedList;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.yacorso.nowaste.models.Food;
import com.yacorso.nowaste.providers.FoodProvider;

import java.util.Arrays;
import java.util.List;


public class BaseAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    SortedList<Food> mFoods;
    Context mContext;
    FoodProvider mFoodProvider;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return mFoods == null ? 0 : mFoods.size();
    }

    public void setFilter(String queryText, List<Food> foodList) {
        for (Food food: foodList) {
            if (!food.getName().toLowerCase().contains(queryText)) {
                remove(food);
            }
            else {
                add(food);
            }
        }
    }

    // region PageList Helpers
    public Food get(int position) {
        mFoods.recalculatePositionOfItemAt(position);
        return mFoods.get(position);
    }

    public int add(Food item) {
        return mFoods.add(item);
    }

    public int indexOf(Food item) {
        return mFoods.indexOf(item);
    }

    public void updateItem(Food item) {
        mFoods.updateItemAt(indexOf(item), item);
    }

    public void addAll(List<Food> items) {
        mFoods.beginBatchedUpdates();
        for (Food item : items) {
            mFoods.add(item);
        }
        mFoods.endBatchedUpdates();
    }

    public void addAll(Food[] items) {
        addAll(Arrays.asList(items));
    }

    public boolean remove(Food item) {
        return mFoods.remove(item);
    }

    public Food removeItemAt(int index) {
        return mFoods.removeItemAt(index);
    }

    public void clear() {
        mFoods.beginBatchedUpdates();
        //remove items at end, to avoid unnecessary array shifting
        while (mFoods.size() > 0) {
            mFoods.removeItemAt(mFoods.size() - 1);
        }
        mFoods.endBatchedUpdates();
    }
}
