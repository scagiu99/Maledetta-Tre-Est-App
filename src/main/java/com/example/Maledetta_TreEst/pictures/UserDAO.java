package com.example.Maledetta_TreEst.pictures;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.Maledetta_TreEst.post.Author;

import java.util.List;
import java.util.Set;

@Dao
public interface UserDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Author user);

    @Query("SELECT COUNT(*) FROM Author WHERE uid =:uid AND pversion =:pversion")
    int checkPictureVersion(String uid, String pversion);

    @Query("SELECT * FROM Author WHERE uid =:uid")
    Author getPictureFromDB(String uid);
}
