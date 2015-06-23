package com.yacorso.nowaste.view.fragments;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yacorso.nowaste.R;
import com.yacorso.nowaste.view.activities.DrawerActivity;

import butterknife.ButterKnife;

/**
 * Created by quentin on 15/06/15.
 */
public abstract class BaseFragment extends Fragment {

    public DrawerActivity getDrawerActivity() {
        return (DrawerActivity) super.getActivity();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(getLayout(), container, false);
        ButterKnife.inject(this, view);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public @StringRes int getTitle(){
        return R.string.not_title_set;
    }

    protected abstract @LayoutRes int getLayout();
}
