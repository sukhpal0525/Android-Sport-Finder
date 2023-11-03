package com.aston.sportsfinder.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.aston.sportsfinder.model.Game;
import com.aston.sportsfinder.model.User;

import java.util.List;

@Dao
public interface UserDao {

    @Query("SELECT * FROM User")
    List<User> getAll();

    @Insert
    void insertUser(User user);

    @Delete
    void deleteUser(User user);
}
