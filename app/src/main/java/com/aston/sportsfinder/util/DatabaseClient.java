package com.aston.sportsfinder.util;

import android.content.Context;

import androidx.room.Room;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.aston.sportsfinder.AppDatabase;
import com.aston.sportsfinder.dao.GameDao;
import com.aston.sportsfinder.dao.NotificationDao;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DatabaseClient {

    private static DatabaseClient instance;
    private AppDatabase appDatabase;
    public final ExecutorService executorService = Executors.newSingleThreadExecutor();

    private DatabaseClient(Context context) {
        appDatabase = Room.databaseBuilder(context, AppDatabase.class, "SportsApp")
                .fallbackToDestructiveMigration()
                .addCallback(new DatabaseCallback(executorService))
                .build();

        resetDatabase();
    }

    public static synchronized DatabaseClient getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseClient(context);
        }
        return instance;
    }

    public AppDatabase getAppDatabase() {
        return appDatabase;
    }

    private void resetDatabase() {
        executorService.execute(() -> {
            DatabaseCallback callback = new DatabaseCallback(executorService);
            callback.insertTestData(appDatabase.gameDao(), appDatabase.userDao(), appDatabase.notificationDao());
        });
    }
}