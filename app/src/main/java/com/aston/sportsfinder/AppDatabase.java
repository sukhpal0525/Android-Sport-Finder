package com.aston.sportsfinder;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.aston.sportsfinder.dao.GameDao;
import com.aston.sportsfinder.dao.NotificationDao;
import com.aston.sportsfinder.model.Game;
import com.aston.sportsfinder.model.Notification;

@Database(entities = {Game.class, Notification.class}, version = 8, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public abstract GameDao gameDao();
    public abstract NotificationDao notificationDao();

}