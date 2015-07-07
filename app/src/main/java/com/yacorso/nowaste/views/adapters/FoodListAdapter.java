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

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Build;
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
import java.util.Date;
import java.util.List;

import com.yacorso.nowaste.R;
import com.yacorso.nowaste.models.Food;
import com.yacorso.nowaste.providers.FoodProvider;

import butterknife.ButterKnife;

import static com.yacorso.nowaste.utils.Utils.getDateTextFromDate;

public class FoodListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    List<Food> mVisibleFoods;
    List<Food> mFoods;
    FoodProvider mFoodProvider;
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
        mFoodProvider = new FoodProvider();

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
        Date outOfDate = food.getFoodFridge().getOutOfDate();
        holder.outOfDate.setText(getDateTextFromDate(outOfDate));
        setOpenIcon(holder.btnOpenToggle, food);

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
                        mFoodProvider.update(food);
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
                mFoodProvider.update(food);
                setOpenIcon(v, food);
            }

        });

        holder.textZone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: Open AddFoodFragment with food informations
            }
        });
    }

    private void setOpenIcon (View v, Food food) {
        Drawable icon;
        if (food.getFoodFridge().isOpen()) {
            icon = mContext.getResources().getDrawable(R.drawable.food_started);
        }
        else {
            icon = mContext.getResources().getDrawable(R.drawable.food_not_started);
        }

        if(android.os.Build.VERSION.SDK_INT >= 16) {
            v.setBackground(icon);
        }
        else {
            v.setBackgroundDrawable(icon);
        }
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
        TextView outOfDate;
        View textZone;

        public FoodListViewHolder(View itemView) {
            super(itemView);
            tvName = ButterKnife.findById(itemView, R.id.txt_food_name);
            btnQuantity = ButterKnife.findById(itemView, R.id.btn_food_quantity);
            btnFavoriteToggle = ButterKnife.findById(itemView, R.id.btn_favorite_toggle);
            btnOpenToggle = ButterKnife.findById(itemView, R.id.btn_open_toggle);
            outOfDate = ButterKnife.findById(itemView, R.id.out_of_date_textview);
            textZone = ButterKnife.findById(itemView, R.id.item_text_zone);

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
