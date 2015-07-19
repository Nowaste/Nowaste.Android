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

import com.raizlabs.android.dbflow.sql.builder.Condition;
import com.raizlabs.android.dbflow.sql.language.Delete;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.yacorso.nowaste.dao.CustomListDao;
import com.yacorso.nowaste.data.NowasteApi;
import com.yacorso.nowaste.models.CustomList;
import com.yacorso.nowaste.models.CustomList$Table;
import com.yacorso.nowaste.models.Fridge;
import com.yacorso.nowaste.models.User;
import com.yacorso.nowaste.utils.LogUtil;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by quentin on 05/07/15.
 */
public class CustomListProvider extends Provider<CustomList, Long>{

    CustomListDao mCustomListDao = new CustomListDao();;


    @Override
    public void create(CustomList item) {
        mCustomListDao.create(item);

        final CustomList itemCreated = item;

        NowasteApi api = NowasteApi.ApiInstance.getInstance();
        api.createCustomList(itemCreated, new Callback<CustomList>() {
            @Override
            public void success(CustomList customList, Response response) {
                try {
                    CustomList newItem = (CustomList) itemCreated.clone();
                    newItem.setServerId(customList.getId());
                    mCustomListDao.update(newItem);
                } catch (CloneNotSupportedException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                LogUtil.LOGD(this, error.toString());
            }
        });

    }

    @Override
    public void update(CustomList item) {
        mCustomListDao.update(item);

        if(item.getServerId() != 0){

            CustomList itemUpdated = null;
            try {
                itemUpdated = (CustomList)item.clone();
                itemUpdated.setId(item.getServerId());
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }

            if(itemUpdated != null){

                NowasteApi api = NowasteApi.ApiInstance.getInstance();
                api.updateCustomList(itemUpdated.getId(), itemUpdated, new Callback<CustomList>() {
                    @Override
                    public void success(CustomList customList, Response response) {

                    }

                    @Override
                    public void failure(RetrofitError error) {
                        LogUtil.LOGD(this, "## ERROR ##");
                    }
                });
            }
        }
    }

    @Override
    public void delete(CustomList item) {
        mCustomListDao.delete(item);

        if(item.getServerId() != 0){

            CustomList itemDeleted = null;
            try {
                itemDeleted = (CustomList)item.clone();
                itemDeleted.setId(item.getServerId());
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }

            if(itemDeleted != null){

                NowasteApi api = NowasteApi.ApiInstance.getInstance();
                api.deleteCustomList(itemDeleted.getId(), new Callback<CustomList>() {
                    @Override
                    public void success(CustomList customList, Response response) {

                    }

                    @Override
                    public void failure(RetrofitError error) {

                    }
                });
            }
        }
    }

    @Override
    public CustomList get(Long id) {
        return new Select()
                .from(CustomList.class)
                .where(Condition.column(CustomList$Table.ID).is(id))
                .querySingle();
    }

    @Override
    public List<CustomList> all() {
        return new Select().from(CustomList.class).queryList();
    }

    public CustomList getCurrentCustomList(User user){
        CustomList customList=null;
        if( user != null){
            if( user.getCustomLists().size() > 0){
                customList = user.getCustomLists().get(0);
            }
        }

        return customList;
    }

}
