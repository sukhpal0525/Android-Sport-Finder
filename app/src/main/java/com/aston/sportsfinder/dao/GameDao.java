package com.aston.sportsfinder.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import com.aston.sportsfinder.model.Game;
import com.aston.sportsfinder.model.GameNotifications;

import java.util.List;

@Dao
public interface GameDao {

    @Query("SELECT * FROM Game")
    List<Game> getAllGames();

    @Insert
    void insertGame(Game game);

    @Delete
    void deleteGame(Game game);

    @Query("SELECT * FROM Game WHERE userId = :userId")
    List<Game> getGamesForUser(int userId);

    @Transaction
    @Query("SELECT * FROM Game WHERE id = :gameId")
    GameNotifications getGameNotifications(int gameId);

    @Query("UPDATE Game SET isJoined = :isJoined WHERE id = :gameId")
    void updateGameJoinStatus(int gameId, boolean isJoined);

    @Query("SELECT * FROM Game WHERE isJoined = 1")
    LiveData<List<Game>> getJoinedGames();

    @Query("UPDATE Game SET isJoined = :isJoined, userId = :userId WHERE id = :gameId")
    void updateGameJoinStatus(int gameId, boolean isJoined, int userId);
}