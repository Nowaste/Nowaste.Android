/*
 * Copyright (c) 2015.
 *
 * "THE BEER-WARE LICENSE" (Revision 42):
 * Quentin Bontemps <q.bontemps@gmail> , Florian Garnier <reventlov@tuta.io>
 * and Marjorie Deboté <marjorie.debote@free.com> wrote this file.
 * As long as you retain this notice you can do whatever you want with this stuff.
 * If we meet some day, and you think this stuff is worth it, you can buy me a beer in return.
 *
 * NoWaste team
 */

package com.yacorso.nowaste.providers;

import com.yacorso.nowaste.dao.UserDao;
import com.yacorso.nowaste.data.NowasteApi;
import com.yacorso.nowaste.models.User;
import com.yacorso.nowaste.utils.LogUtil;

import java.util.List;

public class ConfigurationProvider extends Provider<User, Long> {

    UserDao mUserDao = new UserDao();

    public ConfigurationProvider() {
        super();
    }
    public ConfigurationProvider(NowasteApi api) {
        super(api);
    }

    @Override
    public void create(User item) {
        if(isCreatable(item)){
            mUserDao.create(item);
        }
    }

    @Override
    public void update(User item) {
        if(isCreatable(item)){
            mUserDao.update(item);
        }
    }

    @Override
    public void delete(User item) {
        mUserDao.delete(item);
    }

    @Override
    public User get(Long id) {
        User user = mUserDao.get(id);

        return user;
    }

    @Override
    public List<User> all() {

        List<User> users = mUserDao.all();

        return users;
    }

    private boolean isCreatable(User item){
        boolean isCreatable = false;

        if (!item.isEmpty()) {
            isCreatable = true;
        } else {
            LogUtil.LOGE(this, "item is empty !");
        }

        return isCreatable;
    }
}
