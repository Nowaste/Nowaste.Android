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

package com.yacorso.nowaste.dao;

import com.raizlabs.android.dbflow.sql.builder.Condition;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.raizlabs.android.dbflow.structure.AsyncModel;
import com.raizlabs.android.dbflow.structure.Model;
import com.yacorso.nowaste.events.ConfigurationCreatedEvent;
import com.yacorso.nowaste.events.ConfigurationDeletedEvent;
import com.yacorso.nowaste.events.ConfigurationUpdatedEvent;
import com.yacorso.nowaste.models.Configuration;
import com.yacorso.nowaste.models.Configuration$Table;

import java.util.List;

import de.greenrobot.event.EventBus;


/**
 * UserDao
 * Relation with database
 */
public class ConfigurationDao extends Dao<Configuration, Long> {

    public void transact(final Configuration item, final int type) {
        final AsyncModel.OnModelChangedListener resultConfiguration = new AsyncModel.OnModelChangedListener() {
            @Override
            public void onModelChanged(Model model) {
                Configuration config = (Configuration) model;
                if (type == TYPE_CREATE) {
                    EventBus.getDefault().post(new ConfigurationCreatedEvent(config));
                } else if (type == TYPE_UPDATE) {
                    EventBus.getDefault().post(new ConfigurationUpdatedEvent(config));
                } else if (type == TYPE_DELETE) {
                    EventBus.getDefault().post(new ConfigurationDeletedEvent(config));
                }
            }
        };

        if (type == TYPE_CREATE) {
            item.async().withListener(resultConfiguration).save();
        }
        else if (type == TYPE_UPDATE) {
            item.async().withListener(resultConfiguration).update();
        }
        else if (type == TYPE_DELETE) {
            item.async().withListener(resultConfiguration).delete();
        }
    };

    @Override
    public void create(Configuration item) {
        transact(item, TYPE_CREATE);
    }

    @Override
    public void update(Configuration item) {
        transact(item, TYPE_UPDATE);
    }

    @Override
    public void delete(Configuration item) {
        transact(item, TYPE_DELETE);
    }

    @Override
    public Configuration get(Long id) {
        return new Select()
                .from(Configuration.class)
                .where(Condition.column(Configuration$Table.ID).is(id))
                .querySingle();
    }

    @Override
    public List<Configuration> all() {
        return new Select().from(Configuration.class).queryList();
    }
}
