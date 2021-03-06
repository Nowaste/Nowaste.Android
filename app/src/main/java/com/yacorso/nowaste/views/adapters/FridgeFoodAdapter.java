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
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.util.SortedList;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.util.SortedListAdapterCallback;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;

import java.util.Date;
import java.util.List;

import com.yacorso.nowaste.R;
import com.yacorso.nowaste.events.CallAddFoodToCustomListEvent;
import com.yacorso.nowaste.events.CallUpdateFoodEvent;
import com.yacorso.nowaste.models.CustomList;
import com.yacorso.nowaste.models.Food;
import com.yacorso.nowaste.models.FoodFridge;
import com.yacorso.nowaste.models.User;
import com.yacorso.nowaste.providers.FoodProvider;
import com.yacorso.nowaste.utils.LogUtil;

import butterknife.*;
import de.greenrobot.event.EventBus;

import static com.yacorso.nowaste.utils.DateUtils.getDateTextFromDate;
import static com.yacorso.nowaste.utils.DateUtils.setColorCircleFromDate;

public class FridgeFoodAdapter extends BaseAdapter {

    int lastFoodClickedPosition;

    public FridgeFoodAdapter() {
        mFoodProvider = new FoodProvider();
        foodList = new SortedList<>(Food.class, new SortedListAdapterCallback<Food>(this) {
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

        final Food food = foodList.get(position);

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

                        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
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

                        builder.setNegativeButton(android.R.string.cancel, null);

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
                        EventBus.getDefault().post(new CallAddFoodToCustomListEvent(food));
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
            icon = ContextCompat.getDrawable(mContext, R.drawable.food_started);
        }
        else {
            icon = ContextCompat.getDrawable(mContext, R.drawable.food_not_started);
        }

        if(android.os.Build.VERSION.SDK_INT >= 16) {
            v.setBackground(icon);
        }
        else {
            v.setBackgroundDrawable(icon);
        }
    }

    public static class FridgeViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.txt_food_name) TextView tvName;
        @Bind(R.id.btn_food_quantity) TextView btnQuantity;
        @Bind(R.id.btn_favorite_toggle) Button btnFavoriteToggle;
        @Bind(R.id.btn_open_toggle) Button btnOpenToggle;
        @Bind(R.id.out_of_date_textview) TextView outOfDate;
        @Bind(R.id.item_text_zone) View textZone;

        public FridgeViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @Override
    public void updateItem(Food item) {
        LogUtil.LOGD(this, "UpdateItem");
        foodList.recalculatePositionOfItemAt(lastFoodClickedPosition);
        foodList.updateItemAt(indexOf(item), item);
    }
}
