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

    private static final int TYPE_HEADER = 2;
    private static final int TYPE_ITEM = 1;

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
        RecyclerView.ViewHolder vh ;

        if (viewType == TYPE_ITEM) {
            view = LayoutInflater.from(context).inflate(R.layout.card_food_item, parent, false);
            vh = new FoodListViewHolder(view);
        } else if (viewType == TYPE_HEADER) {
            view = LayoutInflater.from(context).inflate(R.layout.food_list_header, parent, false);
            vh = new FoodListHeaderViewHolder(view);
        } else {
            throw new RuntimeException("There is no type that matches the type " + viewType +
                    " + make sure your using types    correctly");
        }
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_food_item, parent, false);

        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if (!isPositionHeader(position)) {
            FoodListViewHolder holder = (FoodListViewHolder) viewHolder;
            holder.name.setText(foods.get(position - 1).getName());
        }
    }

    public int getBasicItemCount() {
        return foods == null ? 0 : foods.size();
    }

    @Override
    public int getItemCount() {
        return getBasicItemCount() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position)) {
            return TYPE_HEADER;
        }
        return TYPE_ITEM;
    }

    private boolean isPositionHeader(int position) {
        return position == 0;
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

    public static class FoodListHeaderViewHolder extends RecyclerView.ViewHolder {
        public FoodListHeaderViewHolder(View itemView) {
            super(itemView);
        }
    }

}
