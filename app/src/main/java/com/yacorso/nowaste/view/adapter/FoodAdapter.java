package com.yacorso.nowaste.view.adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import com.yacorso.nowaste.R;
import com.yacorso.nowaste.model.Food;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.FoodViewHolder> {


    List<Food> foods;

    public FoodAdapter() {}

    public FoodAdapter(List<Food> foods){
        this.foods = foods;
    }

    public void setFoods(List<Food> foods) {
        this.foods = foods;
    }

    @Override
    public FoodViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_food_item, parent, false);

        FoodViewHolder fvh = new FoodViewHolder(view);

        return fvh;
    }

    @Override
    public void onBindViewHolder(FoodViewHolder holder, int position) {
        holder.name.setText(foods.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return this.foods.size();
    }

    public static class FoodViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener
    {

        CardView cv;
        TextView name;


        public FoodViewHolder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.cv_food_item);
            name = (TextView) itemView.findViewById(R.id.txt_food_name);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {

            Toast.makeText(v.getContext(), "OnClick" ,
                    Toast.LENGTH_LONG).show();

        }

        @Override
        public boolean onLongClick(View v) {

            Toast.makeText(v.getContext(), "OnLongClick" ,
                    Toast.LENGTH_LONG).show();



            return false;
        }
    }

}
