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

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import com.yacorso.nowaste.R;
import com.yacorso.nowaste.models.Food;
import com.yacorso.nowaste.services.FoodService;

public class FoodListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    List<Food> mVisibleFoods;
    List<Food> mFoods;
    FoodService mFoodService;
    Context mContext;

    public FoodListAdapter() {
    }

    public FoodListAdapter(List<Food> foods) {
        mFoods = foods;
        mVisibleFoods = foods;
    }

    public void setFoods(List<Food> foods) {
        mFoods = foods;
        mVisibleFoods = foods;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        mContext = parent.getContext();
        mFoodService = new FoodService();

        View view = LayoutInflater.from(mContext).inflate(R.layout.card_food_item, parent, false);
        RecyclerView.ViewHolder vh = new FoodListViewHolder(view);

        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        FoodListViewHolder holder = (FoodListViewHolder) viewHolder;

        final Food food = mVisibleFoods.get(position);

//        final Food food = mFoods.get(position);
        final int quantity = food.getFoodFridge().getQuantity();

        holder.tvName.setText(food.getName());
        holder.btnQuantity.setText(Integer.toString(quantity));


        /*
         *  Popup changement quantité
         */
        holder.btnQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final NumberPicker numberPicker = new NumberPicker(mContext);
                numberPicker.setMinValue(1);
                numberPicker.setMaxValue(100);
                numberPicker.setValue(quantity);

                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setMessage(R.string.title_quantity_number_picker);
                builder.setView(numberPicker);

                builder.setPositiveButton(R.string.validate, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        /*
                         * Save the modification
                         */
                        int selectedValue = numberPicker.getValue();
                        food.getFoodFridge().setQuantity(selectedValue);
                        mFoodService.update(food);
                    }
                });

                builder.setNegativeButton(R.string.cancel, null);

                builder.create().show();
            }
        });

        holder.btnOpenToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                food.toggleOpen();
                mFoodService.update(food);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mVisibleFoods == null ? 0 : mVisibleFoods.size();
    }

    public void flushFilter(){
        mVisibleFoods=new ArrayList<>();
        mVisibleFoods.addAll(mFoods);
        notifyDataSetChanged();
    }

    public void setFilter(String queryText) {

        mVisibleFoods = new ArrayList<>();
        //constraint = constraint.toString().toLowerCase();
        for (Food food: mFoods) {
            if (food.getName().toLowerCase().contains(queryText))
                mVisibleFoods.add(food);
        }
        notifyDataSetChanged();
    }

    public static class FoodListViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener, View.OnLongClickListener {

        CardView cv;
        TextView tvName;
        TextView btnQuantity;
        Button btnFavoriteToggle;
        Button btnOpenToggle;


        public FoodListViewHolder(View itemView) {
            super(itemView);
//            cv = (CardView) itemView.findViewById(R.id.cv_food_item);
            tvName = (TextView) itemView.findViewById(R.id.txt_food_name);
            btnQuantity = (TextView) itemView.findViewById(R.id.btn_food_quantity);
            btnFavoriteToggle = (Button) itemView.findViewById(R.id.btn_favorite_toggle);
            btnOpenToggle = (Button) itemView.findViewById(R.id.btn_open_toggle);


            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {

            Toast.makeText(v.getContext(), "OnClick",
                    Toast.LENGTH_SHORT).show();

        }

        @Override
        public boolean onLongClick(View v) {

            Toast.makeText(v.getContext(), "OnLongClick",
                    Toast.LENGTH_SHORT).show();


            return false;
        }
    }
}
