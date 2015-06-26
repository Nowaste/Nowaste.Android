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

import com.google.gson.annotations.Expose;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ModelContainer;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.sql.builder.Condition;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.raizlabs.android.dbflow.structure.cache.BaseCacheableModel;
import com.yacorso.nowaste.data.NowasteDatabase;

import java.util.List;

@ModelContainer
@Table(databaseName = NowasteDatabase.NAME)
public class User extends BaseCacheableModel {

    /**
     * Attributes
     */

    @Column
    @Expose
    @PrimaryKey(autoincrement = true)
    protected long id;

    @Column
    @Expose
    protected String firstName;

    @Column
    @Expose
    protected String lastName;

    @Column
    @Expose
    protected String salt;

    @Column
    @Expose
    protected String email;

    @Column
    @Expose
    protected boolean enabled;

    @Column
    @Expose
    protected String password;

    protected List<Fridge> fridges;

    protected List<CustomList> customLists;


    /**
     * Functions
     */
    public User () { }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) { this.email = email; }

    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public List<Fridge> getFridges() {
        if(fridges == null){
            fridges = new Select().from(Fridge.class)
                    .where(Condition.column(Fridge$Table.USER_USER_ID).is(id)).queryList();
        }

        return fridges;
    }


    /**
     * TODO: METHODE A RETIRER UNE FOIS LE DEVELOPPEMENT PASSÉ
     * UTILISÉ SEULEMENT DANS L'INITIALISATION DE L'APP
     * @param fridge
     */
    public void addFridge(Fridge fridge){
        fridges.add(fridge);
    }

    public List<CustomList> getCustomLists() {
        return customLists;
    }

    public boolean isEmpty(){
        return false;
    }

}
