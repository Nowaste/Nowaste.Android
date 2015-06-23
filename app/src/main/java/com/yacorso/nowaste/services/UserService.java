package com.yacorso.nowaste.services;

import com.squareup.otto.Bus;
import com.yacorso.nowaste.dao.UserDao;
import com.yacorso.nowaste.models.User;
import com.yacorso.nowaste.utils.LogUtil;
import com.yacorso.nowaste.webservice.NowasteApi;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by quentin on 22/06/15.
 */
public class UserService extends Service<User, Long> {

    UserDao mUserDao = new UserDao();

    public UserService(Bus bus) {
        super(bus);
    }
    public UserService(NowasteApi api, Bus bus) {
        super(api, bus);
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
