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

package com.yacorso.nowaste.view.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import com.yacorso.nowaste.R;
import com.yacorso.nowaste.models.Food;

public class FoodListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<Food> foods;

    public FoodListAdapter() {
    }

    public FoodListAdapter(List<Food> foods) {
        this.foods = foods;
    }

    public void setFoods(List<Food> foods) {
        this.foods = foods;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;
        Context context = parent.getContext();
        RecyclerView.ViewHolder vh;

        view = LayoutInflater.from(context).inflate(R.layout.card_food_item, parent, false);
        vh = new FoodListViewHolder(view);

        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        FoodListViewHolder holder = (FoodListViewHolder) viewHolder;
        Food food = foods.get(position);
        holder.name.setText(food.getName());
        holder.quantity.setText(Integer.toString(food.getFoodFridge().getQuantity()));
    }

    @Override
    public int getItemCount() {
        return foods == null ? 0 : foods.size();
    }

    public static class FoodListViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener, View.OnLongClickListener {

        CardView cv;
        TextView name;
        TextView quantity;

        public FoodListViewHolder(View itemView) {
            super(itemView);
//            cv = (CardView) itemView.findViewById(R.id.cv_food_item);
            name = (TextView) itemView.findViewById(R.id.txt_food_name);
            quantity = (TextView) itemView.findViewById(R.id.txt_food_quantity);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {

            Toast.makeText(v.getContext(), "OnClick",
                    Toast.LENGTH_LONG).show();

        }

        @Override
        public boolean onLongClick(View v) {

            Toast.makeText(v.getContext(), "OnLongClick",
                    Toast.LENGTH_LONG).show();


            return false;
        }
    }
}
