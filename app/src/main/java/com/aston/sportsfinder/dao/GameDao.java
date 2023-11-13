package com.aston.sportsfinder.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.aston.sportsfinder.model.Game;

import java.util.List;

@Dao
public interface GameDao {

    @Query("SELECT * FROM Game")
    List<Game> getAllGames();

    @Insert
    void insertGame(Game game);

    @Delete
    void deleteGame(Game game);
}