package com.yacorso.nowaste.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

import com.yacorso.nowaste.model.Food;

public class FoodAdapter extends BaseAdapter {
    private ArrayList<Food> dataset;

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        return null;
    }

    // Provide a reference to the views for each com.yacorso.nowaste.data item
    // Complex com.yacorso.nowaste.data items may need more than one com.yacorso.nowaste.view per item, and
    // you provide access to all the views for a com.yacorso.nowaste.data item in a com.yacorso.nowaste.view holder
   /* public static class ViewHolder extends RecyclerView.ViewHolder {
        // each com.yacorso.nowaste.data item is just a string in this case
        public TextView textView;
        public ViewHolder(TextView v) {
            super(v);
            textView = v;
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public FoodAdapter(String[] myDataset) {
        dataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public FoodAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new com.yacorso.nowaste.view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_text_view, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a com.yacorso.nowaste.view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the com.yacorso.nowaste.view with that element
        holder.textView.setText(dataset.indexOf(position));

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return dataset.size();
    }
    */

}
