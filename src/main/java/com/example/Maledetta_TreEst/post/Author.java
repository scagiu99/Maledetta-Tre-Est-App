package com.example.Maledetta_TreEst.post;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Author {

    @PrimaryKey
    @NonNull
    private final String uid;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "pversion")
    private String pversion;

    @ColumnInfo(name = "picture")
    private String picture;
    
    public Author(String name, @NonNull String uid, String pversion, String picture) {
        this.name = name;
        this.uid = uid;
        this.pversion = pversion;
        this.picture = picture;
    }

    @NonNull
    public String getUid() {
        return uid;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public void setPversion(String pversion) {
        this.pversion = pversion;
    }

    public void setName(String name) { this.name = name;}

    public String getPversion() {
        return pversion;
    }

    public String getName() {
        return name;
    }
}
