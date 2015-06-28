package com.yacorso.nowaste.view.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import com.yacorso.nowaste.R;
import com.yacorso.nowaste.models.Food;

public class FoodListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<Food> foodList;
    List<Food> visibleFoods;

    public FoodListAdapter() {
    }

    public FoodListAdapter(List<Food> foods) {
        foodList = foods;
        visibleFoods = foods;
    }

    public void setFoods(List<Food> foods) {
        foodList = foods;
        visibleFoods = foods;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;
        Context context = parent.getContext();
        RecyclerView.ViewHolder vh ;

        view = LayoutInflater.from(context).inflate(R.layout.card_food_item, parent, false);
        vh = new FoodListViewHolder(view);

        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
            FoodListViewHolder holder = (FoodListViewHolder) viewHolder;
            holder.name.setText(visibleFoods.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return visibleFoods == null ? 0 : visibleFoods.size();
    }

    public void flushFilter(){
        visibleFoods=new ArrayList<>();
        visibleFoods.addAll(foodList);
        notifyDataSetChanged();
    }

    public void setFilter(String queryText) {

        visibleFoods = new ArrayList<>();
        //constraint = constraint.toString().toLowerCase();
        for (Food food: foodList) {
            if (food.getName().toLowerCase().contains(queryText))
                visibleFoods.add(food);
        }
        notifyDataSetChanged();
    }

    public static class FoodListViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener, View.OnLongClickListener {

        CardView cv;
        TextView name;


        public FoodListViewHolder(View itemView) {
            super(itemView);
//            cv = (CardView) itemView.findViewById(R.id.cv_food_item);
            name = (TextView) itemView.findViewById(R.id.txt_food_name);

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
