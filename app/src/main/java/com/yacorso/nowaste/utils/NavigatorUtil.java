
/*
 * Copyright (c) 2015.
 *
 * "THE BEER-WARE LICENSE" (Revision 42):
 * <q.bontemps@gmail> , <reventlov@tuta.io> and <marjorie.debote@free.com> wrote this file.
 *  As long as you retain this notice you can do whatever you want with this stuff.
 *  If we meet some day, and you think this stuff is worth it, you can buy me a beer in return.
 *
 * No Waste team
 *
 */
package com.yacorso.nowaste.utils;

import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;


public class NavigatorUtil {
    @NonNull
    protected final FragmentManager mFragmentManager;

    @IdRes
    protected final int mDefaultContainer;

    /**
     * This constructor should be only called once per
     *
     * @param fragmentManager  Your FragmentManger
     * @param defaultContainer Your container id where the fragments should be placed
     */
    public NavigatorUtil(@NonNull final FragmentManager fragmentManager, @IdRes final int defaultContainer) {
        mFragmentManager = fragmentManager;
        mDefaultContainer = defaultContainer;
    }

    /**
     * @return the current active fragment. If no fragment is active it return null.
     */
    public Fragment getActiveFragment() {
        if (mFragmentManager.getBackStackEntryCount() == 0) {
            return null;
        }
        String tag = mFragmentManager
                .getBackStackEntryAt(mFragmentManager.getBackStackEntryCount() - 1).getName();
        return mFragmentManager.findFragmentByTag(tag);
    }

    /**
     * Pushes the fragment, and add it to the history (BackStack)
     *
     * @param fragment the fragment which
     */
    public void goTo(final Fragment fragment) {
        mFragmentManager.beginTransaction()
                .addToBackStack(getName(fragment))
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .replace(mDefaultContainer, fragment, getName(fragment))
                .commit();
        mFragmentManager.executePendingTransactions();
    }

    /**
     * This is just a helper method which returns the simple name of
     * the fragment.
     *
     * @param fragment that get added to the history (BackStack)
     * @return the simple name of the given fragment
     */
    protected String getName(final Fragment fragment) {
        return fragment.getClass().getSimpleName();
    }

    /**
     * Set the new root fragment. If there is any entry in the history (BackStack)
     * it will be deleted.
     *
     * @param startFragment the new root fragment
     */
    public void setRootFragment(final Fragment startFragment) {
        if (getSize() > 0) {
            this.clearHistory();
        }
        this.replaceFragment(startFragment);
    }

    /**
     * Replace the current fragment with the given one, without to add it to backstack.
     * So when the users navigates away from the given fragment it will not appaer in
     * the history.
     *
     * @param fragment the new fragment
     */
    private void replaceFragment(final Fragment fragment) {
        mFragmentManager.beginTransaction()
                .replace(mDefaultContainer, fragment, getName(fragment))
                .commit();
        mFragmentManager.executePendingTransactions();
    }

    /**
     * Goes one entry back in the history
     */
    public void goOneBack() {
        mFragmentManager.popBackStackImmediate();
    }

    /**
     * @return The current size of the history (BackStack)
     */
    public int getSize() {
        return mFragmentManager.getBackStackEntryCount();
    }

    /**
     * @return True if no Fragment is in the History (BackStack)
     */
    public boolean isEmpty() {
        return getSize() == 0;
    }

    /**
     * Goes the whole history back until to the first fragment in the history.
     * It would be the same if the user would click so many times the back button until
     * he reach the first fragment of the app.
     */
    public void gotToTheRootFragmentBack() {
        for (int i = 0; i <= mFragmentManager.getBackStackEntryCount(); ++i) {
            goOneBack();
        }
    }

    /**
     * Clears the whole history so it will no BackStack entry there any more.
     */
    public void clearHistory() {
        //noinspection StatementWithEmptyBody - it works as wanted
        while (mFragmentManager.popBackStackImmediate()) ;
    }
}
