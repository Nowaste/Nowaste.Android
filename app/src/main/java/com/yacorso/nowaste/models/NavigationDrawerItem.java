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

package com.yacorso.nowaste.models;

import com.yacorso.nowaste.views.fragments.BaseFragment;

/**
 * Created by quentin on 24/06/15.
 */
public class NavigationDrawerItem {
    protected boolean showNotify;
    protected String title;
    protected int icon;
    protected BaseFragment fragment;
    protected long id;
    protected Class type;

    public NavigationDrawerItem() {

    }

    public NavigationDrawerItem(String title, int icon, BaseFragment fragment, long id, Class type) {
        this.title = title;
        this.icon = icon;
        this.fragment = fragment;
        this.id = id;
        this.type = type;

    }

    public boolean isShowNotify() {
        return showNotify;
    }

    public void setShowNotify(boolean showNotify) {
        this.showNotify = showNotify;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public BaseFragment getFragment() {
        return fragment;
    }

    public void setFragment(BaseFragment fragment) {
        this.fragment = fragment;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Class getType() {
        return type;
    }

    public void setType(Class type) {
        this.type = type;
    }
}
