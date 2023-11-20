package com.aston.sportsfinder.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.aston.sportsfinder.model.Game;
import com.aston.sportsfinder.model.User;

import java.util.List;

@Dao
public interface UserDao {

    @Query("SELECT COUNT(id) FROM User")
    int countUsers();

    @Insert
    void insertUser(User user);

    @Delete
    void deleteUser(User user);

    @Query("SELECT id from User LIMIT 1")
    Integer getCurrentUserId();
}