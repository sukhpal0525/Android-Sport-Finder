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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

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