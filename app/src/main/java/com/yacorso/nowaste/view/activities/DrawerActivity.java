package com.yacorso.nowaste.view.activities;

import android.content.res.Configuration;
import android.support.annotation.IdRes;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.yacorso.nowaste.R;
import com.yacorso.nowaste.utils.LogUtil;
import com.yacorso.nowaste.utils.NavigatorUtil;
import com.yacorso.nowaste.view.fragments.BaseFragment;
import com.yacorso.nowaste.view.fragments.FoodListFragment;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.greenrobot.event.EventBus;

public class DrawerActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    @InjectView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

    @InjectView(R.id.navigation_view)
    NavigationView mNavigationView;

    private static NavigatorUtil mNavigator;
    private Toolbar mToolbar;
    private ActionBarDrawerToggle mDrawerToggle;
    private @IdRes int mCurrentMenuItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);

        ButterKnife.inject(this);

        initActionBarAndNavDrawer();
        initNavigator();

        mCurrentMenuItem = R.id.menu_item_my_fridge;
        setNewRootFragment(FoodListFragment.newInstance());
    }

    private void initActionBarAndNavDrawer() {
        mToolbar = ButterKnife.findById(this, R.id.toolbar);
        setSupportActionBar(mToolbar);

        mDrawerToggle = new ActionBarDrawerToggle(
                this, mDrawerLayout,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);

        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        mDrawerToggle.syncState();
        mNavigationView.setNavigationItemSelectedListener(this);
    }

    private void initNavigator() {
        if(mNavigator != null) return;
        mNavigator = new NavigatorUtil(getSupportFragmentManager(), R.id.container);
    }

    private void setNewRootFragment(BaseFragment fragment){
        mNavigator.setRootFragment(fragment);
        getSupportActionBar().setTitle(fragment.getTitle());
        mDrawerLayout.closeDrawers();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        @IdRes int id = menuItem.getItemId();
        if(id == mCurrentMenuItem) {
            mDrawerLayout.closeDrawers();
            return false;
        }
        switch (id){
            case R.id.menu_item_my_fridge:
                Toast.makeText(this, "Mon frigo", Toast.LENGTH_SHORT).show();
//                setNewRootFragment(StandardAppBarFragment.newInstance());
                break;
            case R.id.menu_item_settings:
                Toast.makeText(this, "Paramètres", Toast.LENGTH_SHORT).show();

//                setNewRootFragment(TabHolderFragment.newInstance());
                break;
        }
        mCurrentMenuItem = id;
        menuItem.setChecked(true);
        return false;
    }

    @Override
    public void finish() {
        mNavigator = null;
        super.finish();
    }

    private void openFragment (BaseFragment fragment) {
        mNavigator.goTo(fragment);
        getSupportActionBar().setTitle(fragment.getTitle());
    }
}
