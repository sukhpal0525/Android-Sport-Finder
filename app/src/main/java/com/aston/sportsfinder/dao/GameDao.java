package com.aston.sportsfinder.dao;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import com.aston.sportsfinder.model.Game;

import java.util.List;

@Dao
public interface GameDao {

    @Query("SELECT * FROM Game")
    List<Game> getAllGames();

    @Insert
    void insertGame(Game game);

    @Query("SELECT COUNT(id) FROM Game")
    int countGames();

    @Query("UPDATE Game SET isJoined = :isJoined, userId = :userId WHERE id = :gameId")
    void updateGameJoinStatus(int gameId, boolean isJoined, int userId);

    @Query("SELECT COUNT(id) FROM Game WHERE id = :gameId AND userId = :userId AND isJoined = 1")
    boolean isGameJoined(int gameId, int userId);

    @Query("SELECT * FROM Game WHERE userId = :userId ORDER BY date, time LIMIT 2")
    List<Game> getUpcomingGames(int userId);

    @Query("SELECT * FROM Game WHERE (:query IS NULL OR team1 LIKE '%' || :query || '%' OR team2 LIKE '%' || :query || '%' OR gameType LIKE '%' || :query || '%')")
    List<Game> searchGames(@Nullable String query);

    @Query("SELECT * FROM Game WHERE id = :gameId")
    Game getGameById(int gameId);
}