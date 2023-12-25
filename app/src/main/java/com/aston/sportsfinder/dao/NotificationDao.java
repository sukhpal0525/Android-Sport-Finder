package com.aston.sportsfinder.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.aston.sportsfinder.model.Notification;

import java.util.List;

@Dao
public interface NotificationDao {

    @Insert
    void insertNotification(Notification notification);

    @Query("SELECT * FROM Notification WHERE userId = :userId ORDER BY timestamp DESC")
    List<Notification> getNotifications(int userId);

    @Query("UPDATE Notification SET isRead = 1 WHERE userId = :userId")
    void markNotificationsAsRead(int userId);

    @Query("SELECT COUNT(*) FROM Notification WHERE userId = :userId AND isRead = 0")
    int getUnreadNotificationCount(int userId);

    @Query("DELETE FROM Notification WHERE userId = :userId AND gameId = :gameId")
    void removeNotification(int userId, int gameId);

    @Query("SELECT Notification.* FROM Notification " +
            "JOIN Game ON Notification.gameId = Game.id " +
            "WHERE Notification.userId = :userId AND (" +
            "Game.gameType || ' ' || Game.team1 || ' ' || Game.team2 LIKE '%' || :query || '%')")
    List<Notification> searchForNotification(int userId, String query);
}