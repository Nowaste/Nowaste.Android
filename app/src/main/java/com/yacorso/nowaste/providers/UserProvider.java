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

package com.yacorso.nowaste.providers;

import com.yacorso.nowaste.dao.UserDao;
import com.yacorso.nowaste.models.User;
import com.yacorso.nowaste.utils.LogUtil;
import com.yacorso.nowaste.data.NowasteApi;

import java.util.List;

public class UserProvider extends Provider<User, Long> {

    UserDao mUserDao = new UserDao();

    public UserProvider() {
        super();
    }
    public UserProvider(NowasteApi api) {
        super(api);
    }

    @Override
    public void create(User item) {
        mUserDao.create(item);
    }

    @Override
    public void update(User item) {
        mUserDao.update(item);
    }

    @Override
    public void delete(User item) {
        mUserDao.delete(item);
    }

    @Override
    public User get(Long id) {
        User user = mUserDao.get(id);
        this.sync();

        return user;
    }

    @Override
    public List<User> all() {
        List<User> users = mUserDao.all();

        return users;
    }
}
