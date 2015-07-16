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
import com.yacorso.nowaste.models.CustomList;
import com.yacorso.nowaste.models.CustomList$Table;
import com.yacorso.nowaste.models.Fridge;
import com.yacorso.nowaste.models.User;
import com.yacorso.nowaste.utils.LogUtil;

import java.util.List;

/**
 * Created by quentin on 05/07/15.
 */
public class CustomListProvider extends Provider<CustomList, Long>{

    CustomListDao mCustomListDao = new CustomListDao();;


    @Override
    public void create(CustomList item) {
        mCustomListDao.create(item);
    }

    @Override
    public void update(CustomList item) {
        mCustomListDao.update(item);
    }

    @Override
    public void delete(CustomList item) {
        new Delete()
                .from(CustomList.class)
                .where(Condition.column(CustomList$Table.ID).is(item.getId()))
                .query();
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
