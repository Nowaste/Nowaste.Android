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

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ModelContainer;
import com.raizlabs.android.dbflow.annotation.OneToMany;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.sql.builder.Condition;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.raizlabs.android.dbflow.structure.cache.BaseCacheableModel;
import com.yacorso.nowaste.data.NowasteDatabase;

import java.util.List;

@ModelContainer
@Table(databaseName = NowasteDatabase.NAME)
public class User extends BaseCacheableModel implements Parcelable {

    /**
     * Attributes
     */

    @Column
    @Expose
    @PrimaryKey(autoincrement = true)
    long id;

    @Column
    @Expose
    String firstName;

    @Column
    @Expose
    String lastName;

    @Column
    @Expose
    String salt;

    @Column
    @Expose
    String email;

    @Column
    @Expose
    boolean enabled;

    @Column
    @Expose
    String password;

    List<Fridge> fridges;

    List<CustomList> customLists;


    /**
     * Functions
     */
    public User() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

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

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @OneToMany(methods = {OneToMany.Method.LOAD}, variableName = "fridges")
    public List<Fridge> getFridges() {
        if (fridges == null) {
            fridges = new Select()
                        .from(Fridge.class)
                        .where(Condition.column(Fridge$Table.USER_USER_ID).is(id))
                        .queryList();
        }
        return fridges;
    }


    /**
     * TODO: METHODE A RETIRER UNE FOIS LE DEVELOPPEMENT PASSÉ
     * UTILISÉ SEULEMENT DANS L'INITIALISATION DE L'APP
     *
     * @param fridge
     */
    public void addFridge(Fridge fridge) {
        fridges.add(fridge);
    }
    public void removeFridge(Fridge fridge) {
        fridges.remove(fridge);
    }

    public void addCustomList(CustomList customList) {
        customLists.add(customList);
    }

    @OneToMany(methods = {OneToMany.Method.LOAD}, variableName = "customLists")
    public List<CustomList> getCustomLists() {
        if (customLists == null) {
            customLists = new Select()
                            .from(CustomList.class)
                            .where(Condition.column(CustomList$Table.USER_USER_ID).is(id))
                            .queryList();
        }
        return customLists;
    }

    public void removeCustomList(CustomList customList) {
        customLists.remove(customList);
    }



    public boolean isEmpty() {
        return false;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeLong(this.id);
        out.writeString(this.firstName);
    }
}
