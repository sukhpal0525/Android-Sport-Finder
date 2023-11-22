package com.aston.sportsfinder;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.aston.sportsfinder.dao.GameDao;
import com.aston.sportsfinder.dao.NotificationDao;
import com.aston.sportsfinder.dao.UserDao;
import com.aston.sportsfinder.model.Game;
import com.aston.sportsfinder.model.Notification;
import com.aston.sportsfinder.model.User;

@Database(entities = {Game.class, User.class, Notification.class}, version = 34, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public abstract GameDao gameDao();
    public abstract UserDao userDao();
    public abstract NotificationDao notificationDao();

}