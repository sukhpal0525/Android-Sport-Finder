package com.aston.sportsfinder.model;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "Notification")
public class Notification {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private int userId;
    private String message;
    private long timestamp;
    private boolean isRead;
    private int gameId;

    public Notification(int id, int userId, String message, long timestamp, boolean isRead, int gameId) {
        this.id = id;
        this.userId = userId;
        this.message = message;
        this.timestamp = timestamp;
        this.isRead = isRead;
        this.gameId = gameId;
    }
}

//    @PrimaryKey(autoGenerate = true)
//    private int id;
//    private int gameId;
//    private int userId;
//    private String title;
//    private String message;
//    private long timestamp;
//    private boolean isRead;