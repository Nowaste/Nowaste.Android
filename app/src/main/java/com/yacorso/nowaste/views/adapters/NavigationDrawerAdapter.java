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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yacorso.nowaste.R;
import com.yacorso.nowaste.models.NavigationDrawerItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by quentin on 24/06/15.
 */
public class NavigationDrawerAdapter extends RecyclerView.Adapter
        <NavigationDrawerAdapter.NavigationDrawerViewHolder> {

    SortedList<NavigationDrawerItem> mItems;
    Context mContext;

    public NavigationDrawerAdapter() {
        mItems = new SortedList<>(NavigationDrawerItem.class, new SortedList.Callback<NavigationDrawerItem>() {
            @Override
            public int compare(NavigationDrawerItem o1, NavigationDrawerItem o2) {
                return o1.getTitle().compareTo(o2.getTitle());
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
            public boolean areContentsTheSame(NavigationDrawerItem oldItem, NavigationDrawerItem newItem) {
                // return whether the items' visual representations are the same or not.
                return oldItem.getTitle().equals(newItem.getTitle());
            }

            @Override
            public boolean areItemsTheSame(NavigationDrawerItem item1, NavigationDrawerItem item2) {
                return item1.getTitle() == item2.getTitle();
            }
        });
    }

    @Override
    public NavigationDrawerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();

        View view = LayoutInflater.from(mContext).inflate(R.layout.nav_draw_item, parent, false);
        NavigationDrawerViewHolder vh = new NavigationDrawerViewHolder(view);

        return vh;
    }

    @Override
    public void onBindViewHolder(NavigationDrawerViewHolder holder, int position) {
        NavigationDrawerItem item = mItems.get(position);
        holder.mTxtTitle.setText(item.getTitle());
        holder.mImgIcon.setImageResource(item.getIcon());
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    class NavigationDrawerViewHolder extends RecyclerView.ViewHolder {

        TextView mTxtTitle;
        ImageView mImgIcon;

        public NavigationDrawerViewHolder(View itemView) {
            super(itemView);

            mTxtTitle = (TextView) itemView.findViewById(R.id.title);
            mImgIcon = (ImageView) itemView.findViewById(R.id.list_item_left_icon);
        }
    }

    // region PageList Helpers
    public NavigationDrawerItem get(int position) {
        return mItems.get(position);
    }

    public int add(NavigationDrawerItem item) {
        return mItems.add(item);
    }

    public int indexOf(NavigationDrawerItem item) {
        return mItems.indexOf(item);
    }

    public void updateItemAt(int index, NavigationDrawerItem item) {
        mItems.updateItemAt(index, item);
    }

    public void addAll(List<NavigationDrawerItem> items) {
        mItems.beginBatchedUpdates();
        for (NavigationDrawerItem item : items) {
            mItems.add(item);
        }
        mItems.endBatchedUpdates();
    }

    public void addAll(NavigationDrawerItem[] items) {
        addAll(Arrays.asList(items));
    }

    public boolean remove(NavigationDrawerItem item) {
        return mItems.remove(item);
    }

    public NavigationDrawerItem removeItemAt(int index) {
        return mItems.removeItemAt(index);
    }

    public void clear() {
        mItems.beginBatchedUpdates();
        //remove items at end, to avoid unnecessary array shifting
        while (mItems.size() > 0) {
            mItems.removeItemAt(mItems.size() - 1);
        }
        mItems.endBatchedUpdates();
    }
}
