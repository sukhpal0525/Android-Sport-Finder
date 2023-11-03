package com.aston.sportsfinder.model;

//import com.aston.sportsfinder.service.WeatherAPI;

import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "Game")
public class Game implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String gameType;
    private String team1;
    private String team2;
    private boolean isJoined;

    @Embedded
    private Location location;
    private String date;
    private String dateTime;

//  Foreign key (Game --> User)
//  private int userId;

//  private WeatherAPI weather;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGameType() {
        return gameType;
    }

    public void setGameType(String gameType) {
        this.gameType = gameType;
    }

    public String getTeam1() {
        return team1;
    }

    public void setTeam1(String team1) {
        this.team1 = team1;
    }

    public String getTeam2() {
        return team2;
    }

    public void setTeam2(String team2) {
        this.team2 = team2;
    }

    public boolean isJoined() {
        return isJoined;
    }

    public void setJoined(boolean joined) {
        isJoined = joined;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public enum GameType { FOOTBALL; }
}