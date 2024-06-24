package com.example.Maledetta_TreEst.pictures;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.Maledetta_TreEst.post.Author;

@Database(entities = {Author.class}, version = 1)
public abstract class UserDB extends RoomDatabase {
    public abstract UserDAO userDao();

}
