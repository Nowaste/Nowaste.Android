package com.yacorso.nowaste.model;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ModelContainer;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;

import com.raizlabs.android.dbflow.structure.cache.BaseCacheableModel;
import com.yacorso.nowaste.data.NowasteDatabase;

@ModelContainer
@Table(databaseName = NowasteDatabase.NAME)
public class User extends BaseCacheableModel {

    /**
     * Attributes
     */

    @Column
    @PrimaryKey(autoincrement = true)
    protected int id;
    @Column
    protected String firstName;

    @Column
    protected String lastName;

    @Column
    protected String salt;

    @Column
    protected String email;

    @Column
    protected boolean enabled;

    @Column
    protected String password;


    /**
     * Functions
     */
    public User () { }

    public long getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

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
}
