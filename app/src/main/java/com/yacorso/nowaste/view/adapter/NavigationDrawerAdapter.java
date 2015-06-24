package com.yacorso.nowaste.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yacorso.nowaste.R;
import com.yacorso.nowaste.models.NavigationDrawerItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by quentin on 24/06/15.
 */
public class NavigationDrawerAdapter extends RecyclerView.Adapter
        <NavigationDrawerAdapter.NavigationDrawerViewHolder> {

    List<NavigationDrawerItem> mItems = new ArrayList<NavigationDrawerItem>();
    Context mContext;

    public NavigationDrawerAdapter() {
    }

    public NavigationDrawerAdapter(List<NavigationDrawerItem> items) {
        mItems = items;
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
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public void setItems(List<NavigationDrawerItem> items) {
        mItems = items;
    }

    class NavigationDrawerViewHolder extends RecyclerView.ViewHolder {

        TextView mTxtTitle;

        public NavigationDrawerViewHolder(View itemView) {
            super(itemView);

            mTxtTitle = (TextView) itemView.findViewById(R.id.title);

        }
    }
}
