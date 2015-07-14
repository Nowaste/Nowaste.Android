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
import android.graphics.drawable.Drawable;
import android.support.v7.util.SortedList;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.util.SortedListAdapterCallback;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.yacorso.nowaste.NowasteApplication;
import com.yacorso.nowaste.R;
import com.yacorso.nowaste.events.CallUpdateFoodEvent;
import com.yacorso.nowaste.events.FoodUpdatedEvent;
import com.yacorso.nowaste.models.CustomList;
import com.yacorso.nowaste.models.Food;
import com.yacorso.nowaste.models.FoodFridge;
import com.yacorso.nowaste.models.User;
import com.yacorso.nowaste.providers.CustomListProvider;
import com.yacorso.nowaste.providers.FoodProvider;

import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

import static com.yacorso.nowaste.utils.DateUtils.getDateTextFromDate;
import static com.yacorso.nowaste.utils.DateUtils.setColorCircleFromDate;

public class FridgeFoodAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    SortedList<Food> mFoods;
    Context mContext;
    FoodProvider mFoodProvider;
    int lastFoodClickedPosition;

    public FridgeFoodAdapter() {
        mFoodProvider = new FoodProvider();
        mFoods = new SortedList<>(Food.class, new SortedListAdapterCallback<Food>(this) {
            @Override
            public int compare(Food o1, Food o2) {
                return o1.getFoodFridge().getOutOfDate().compareTo(o2.getFoodFridge().getOutOfDate());
            }

            @Override
            public boolean areContentsTheSame(Food oldItem, Food newItem) {
                // return whether the items' visual representations are the same or not.
                return oldItem.getName().equals(newItem.getName());
            }

            @Override
            public boolean areItemsTheSame(Food item1, Food item2) {
                return item1.getId() == item2.getId();
            }
        });
    }

    public static class FoodListViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener, View.OnLongClickListener {

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

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.card_food_item, parent, false);
        RecyclerView.ViewHolder vh = new FridgeViewHolder(view);

        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
        final FridgeViewHolder holder = (FridgeViewHolder) viewHolder;

        final Food food = mFoods.get(position);

        final int quantity = food.getFoodFridge().getQuantity();

        holder.tvName.setText(food.getName());
        holder.btnQuantity.setText(Integer.toString(quantity));
        Date outOfDate = food.getFoodFridge().getOutOfDate();
        holder.outOfDate.setText(getDateTextFromDate(outOfDate));
        setOpenIcon(holder.btnOpenToggle, food);

        setColorCircleFromDate(holder.btnQuantity, outOfDate, mContext.getResources());

        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                lastFoodClickedPosition = position;
                switch (v.getId()) {
                    case R.id.btn_food_quantity:
                        /*
                         *  Popup changement quantité
                         */
                        final NumberPicker numberPicker = new NumberPicker(mContext);
                        numberPicker.setMinValue(1);
                        numberPicker.setMaxValue(100);
                        numberPicker.setValue(quantity);
                        numberPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

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
                        break;

                    case R.id.btn_open_toggle:
                        /*
                         *  Open/close food
                         */
                        FoodFridge foodFridge = food.getFoodFridge();
                        foodFridge.toggleOpen();
                        setOpenIcon(v, food);
                        Date outOfDate = foodFridge.getOutOfDate();
                        setColorCircleFromDate(holder.btnQuantity, outOfDate, v.getResources());
                        mFoodProvider.update(food);
                        break;

                    case R.id.item_text_zone:
                        EventBus.getDefault().post(new CallUpdateFoodEvent(food));
                        break;
                    case R.id.btn_favorite_toggle:

                        User user = NowasteApplication.sCurrentUser;
//                CustomList customList = user.getCustomList();
                        final List<CustomList> customLists = user.getCustomLists();

                        String[] values = new String[customLists.size()];

                        for (int i = 0; i < customLists.size(); i++) {
                            values[i] = customLists.get(i).getName();
                        }

                        final NumberPicker customListPicker = new NumberPicker(mContext);
                        customListPicker.setMinValue(0);
                        customListPicker.setMaxValue(values.length - 1);
                        customListPicker.setDisplayedValues(values);
                        customListPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

                        AlertDialog.Builder builderCustomList = new AlertDialog.Builder(mContext);
                        builderCustomList.setMessage(R.string.title_select_custom_list);
                        builderCustomList.setView(customListPicker);

                        builderCustomList.setPositiveButton(R.string.validate, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                int index = customListPicker.getValue();
                                CustomList customList = customLists.get(index);

                                if(customList != null){
                                    Food newFood = new Food(food.getName());
                                    newFood.setFoodList(customList);

                                    FoodProvider foodProvider = new FoodProvider();
                                    foodProvider.create(newFood);
                                }
                            }
                        });

                        builderCustomList.setNegativeButton(R.string.cancel, null);

                        builderCustomList.create().show();

                        break;
                }
            }
        };

        holder.btnQuantity.setOnClickListener(clickListener);
        holder.btnOpenToggle.setOnClickListener(clickListener);
        holder.btnFavoriteToggle.setOnClickListener(clickListener);
        holder.textZone.setOnClickListener(clickListener);
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

    private int setColorCircle () {
        return mContext.getResources().getColor(R.color.circle_long);
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

    public static class FridgeViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener, View.OnLongClickListener {

        TextView tvName;
        TextView btnQuantity;
        Button btnFavoriteToggle;
        Button btnOpenToggle;
        TextView outOfDate;
        View textZone;

        public FridgeViewHolder(View itemView) {
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
//            Toast.makeText(v.getContext(), "OnClick",
//                    Toast.LENGTH_SHORT).show();
        }

        @Override
        public boolean onLongClick(View v) {
//            Toast.makeText(v.getContext(), "OnLongClick",
//                    Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    @Override
    public int getItemCount() {
        return mFoods == null ? 0 : mFoods.size();
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
        mFoods.recalculatePositionOfItemAt(lastFoodClickedPosition);
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
