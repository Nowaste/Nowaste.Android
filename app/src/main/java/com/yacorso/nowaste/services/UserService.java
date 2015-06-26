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

package com.yacorso.nowaste.services;

import com.yacorso.nowaste.dao.UserDao;
import com.yacorso.nowaste.models.User;
import com.yacorso.nowaste.utils.LogUtil;
import com.yacorso.nowaste.data.NowasteApi;

import java.util.ArrayList;
import java.util.List;

public class UserService extends Service<User, Long> {

    UserDao mUserDao = new UserDao();

    public UserService() {
        super();
    }
    public UserService(NowasteApi api) {
        super(api);
    }

    @Override
    public void create(User item) {
        if(isCreatable(item)){

            if(true){
                /**
                 * Create to database
                 */
                mUserDao.create(item);

            }else{
                /**
                 * Create to webservice
                 */
            }
        }
    }

    @Override
    public void update(User item) {
        if(isCreatable(item)){

            if(true){
                /**
                 * Create to database
                 */
                mUserDao.update(item);

            }else{
                /**
                 * Create to webservice
                 */
            }
        }
    }

    @Override
    public void delete(User item) {
        if(true){
            mUserDao.delete(item);
        }else{

        }
    }

    @Override
    public User get(Long id) {
        User user = null;

        if(true){
            mUserDao.get(id);
        }

        return user;
    }

    @Override
    public List<User> all() {

        List<User> users = new ArrayList<User>();

        if(true){
            /**
             * Get all users from database
             */
            users = mUserDao.all();
        }else{
            /**
             * Get all users from webservice
             */

        }
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
