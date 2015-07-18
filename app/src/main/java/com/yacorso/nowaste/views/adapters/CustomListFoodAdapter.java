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

import android.support.v7.util.SortedList;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.yacorso.nowaste.R;
import com.yacorso.nowaste.events.CallUpdateFoodEvent;
import com.yacorso.nowaste.models.Food;
import com.yacorso.nowaste.providers.FoodProvider;

import butterknife.*;
import de.greenrobot.event.EventBus;

public class CustomListFoodAdapter extends BaseAdapter {

    public CustomListFoodAdapter() {
        mFoodProvider = new FoodProvider();
        mFoods = new SortedList<>(Food.class, new SortedList.Callback<Food>() {
            @Override
            public int compare(Food o1, Food o2) {
                return o1.getName().compareTo(o2.getName());
            }

            @Override
            public void onInserted(int position, int count) {
                notifyItemRangeInserted(position, count);
            }

            @Override
            public void onRemoved(int position, int count) {
                notifyItemRangeRemoved(position, count);
            }

            @Override
            public void onMoved(int fromPosition, int toPosition) {
                notifyItemMoved(fromPosition, toPosition);
            }

            @Override
            public void onChanged(int position, int count) {
                notifyItemRangeChanged(position, count);
            }

            @Override
            public boolean areContentsTheSame(Food oldItem, Food newItem) {
                return oldItem.getName().equals(newItem.getName());
            }

            @Override
            public boolean areItemsTheSame(Food item1, Food item2) {
                return item1.getId() == item2.getId();
            }
        });
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();

        View view = LayoutInflater.from(mContext).inflate(R.layout.card_food_item2, parent, false);
        RecyclerView.ViewHolder vh = new CustomListViewHolder(view);

        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        CustomListViewHolder holder = (CustomListViewHolder) viewHolder;
        final Food food = mFoods.get(position);

        holder.tvName.setText(food.getName());
        holder.textZone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new CallUpdateFoodEvent(food));
            }
        });
    }

    public static class CustomListViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.txt_food_name) TextView tvName;
        @Bind(R.id.item_text_zone) View textZone;

        public CustomListViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
