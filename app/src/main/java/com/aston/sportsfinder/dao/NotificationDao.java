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

    @Query("SELECT COUNT(id) FROM Notification")
    int countNotifications();

    @Query("SELECT * FROM Notification ORDER BY timestamp DESC")
    LiveData<List<Notification>> getAllNotificationsAt();

    @Query("SELECT * FROM Notification WHERE userId = :userId ORDER BY timestamp DESC")
    LiveData<List<Notification>> getNotificationsForUser(int userId);

    @Query("SELECT * FROM Notification WHERE userId = :userId ORDER BY timestamp DESC")
    List<Notification> getNotificationsForUserSync(int userId);

    @Query("UPDATE Notification SET isRead = 1 WHERE id = :notificationId")
    void markNotificationAsRead(int notificationId);
}