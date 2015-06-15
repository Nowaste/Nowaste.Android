package com.yacorso.nowaste.view.fragments;

import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.raizlabs.android.dbflow.sql.language.Select;
import com.yacorso.nowaste.R;
import com.yacorso.nowaste.model.Food;
import com.yacorso.nowaste.view.adapter.FoodAdapter;

import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class FragmentFoodList extends Fragment {

    RecyclerView rv ;
    LinearLayoutManager llm ;
    FloatingActionButton btnAdd;


    public FragmentFoodList() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_food_list, container, false);
//        btnAddFood = (FloatingActionButton) view.findViewById(R.id.btnAddFood);
//        btnAddFood.setRippleColor()



        rv = (RecyclerView)view.findViewById(R.id.rv_food_list);
        llm = new LinearLayoutManager(getActivity());

        rv.setLayoutManager(llm);

        List<Food> foodList = new Select().from(Food.class).queryList();


        FoodAdapter adapter = new FoodAdapter(foodList);
        rv.setAdapter(adapter);

        FloatingActionButton myFab = (FloatingActionButton)  view.findViewById(R.id.btnAddFood);
        myFab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Snackbar.make(view, "Here's a Snackbar", Snackbar.LENGTH_LONG)
                        .setAction("Action",null).show();
            }
        });


        return view;
    }
}
