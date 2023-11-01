package com.aston.sportsfinder.model;

import com.aston.sportsfinder.service.WeatherAPI;

import java.io.Serializable;

public class Game implements Serializable {

    private int id;
    private String gameType;
    private String team1;
    private String team2;
    private Location location;
    private String date;
    private String dateTime;
    private WeatherAPI weather;

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

    public WeatherAPI getWeather() {
        return weather;
    }

    public void setWeather(WeatherAPI weather) {
        this.weather = weather;
    }

    public enum GameType {
        FOOTBALL;
    }
}