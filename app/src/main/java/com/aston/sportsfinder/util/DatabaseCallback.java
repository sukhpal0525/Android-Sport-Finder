package com.aston.sportsfinder.util;

import androidx.annotation.NonNull;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

public class DatabaseCallback extends RoomDatabase.Callback {

    @Override
    public void onCreate(@NonNull SupportSQLiteDatabase db) {
        super.onCreate(db);
        insertTestData(db);
    }

    public void insertTestData(SupportSQLiteDatabase db) {
        db.execSQL("INSERT INTO Game (gameType, team1, team2, isJoined, street, city, state, country, latitude, longitude, date, time) VALUES ('Football', 'Team A', 'Team B', 0, '123 Broadway St', 'Manhattan', 'NY', 'USA', 40.7831, -73.9712, '2023-10-20', '14:00')");
        db.execSQL("INSERT INTO Game (gameType, team1, team2, isJoined, street, city, state, country, latitude, longitude, date, time) VALUES ('Football', 'Team A', 'Team B', 0, '456 Fulton St', 'Brooklyn', 'NY', 'USA', 40.6782, -73.9442, '2023-10-21', '16:00')");
        db.execSQL("INSERT INTO Game (gameType, team1, team2, isJoined, street, city, state, country, latitude, longitude, date, time) VALUES ('Football', 'Team A', 'Team B', 0, '789 Queens Blvd', 'Queens', 'NY', 'USA', 40.7282, -73.7949, '2023-10-22', '17:00')");
        db.execSQL("INSERT INTO Game (gameType, team1, team2, isJoined, street, city, state, country, latitude, longitude, date, time) VALUES ('Football', 'Team A', 'Team B', 0, '101 Bronx Park S', 'The Bronx', 'NY', 'USA', 40.8448, -73.8648, '2023-10-23', '15:30')");
        db.execSQL("INSERT INTO Game (gameType, team1, team2, isJoined, street, city, state, country, latitude, longitude, date, time) VALUES ('Football', 'Team A', 'Team B', 0, '202 Richmond Ave', 'Staten Island', 'NY', 'USA', 40.5795, -74.1502, '2023-10-24', '13:00')");
        db.execSQL("INSERT INTO Game (gameType, team1, team2, isJoined, street, city, state, country, latitude, longitude, date, time) VALUES ('Football', 'Team A', 'Team B', 0, '50 Central Park West', 'Manhattan', 'NY', 'USA', 40.7713, -73.9794, '2023-10-25', '12:00')");
        db.execSQL("INSERT INTO Game (gameType, team1, team2, isJoined, street, city, state, country, latitude, longitude, date, time) VALUES ('Football', 'Team A', 'Team B', 0, '3050 Eastchester Rd', 'The Bronx', 'NY', 'USA', 40.8694, -73.8429, '2023-10-26', '16:30')");
        db.execSQL("INSERT INTO Game (gameType, team1, team2, isJoined, street, city, state, country, latitude, longitude, date, time) VALUES ('Football', 'Team A', 'Team B', 0, '529 Flatbush Ave', 'Brooklyn', 'NY', 'USA', 40.6627, -73.9596, '2023-10-27', '14:30')");
        db.execSQL("INSERT INTO Game (gameType, team1, team2, isJoined, street, city, state, country, latitude, longitude, date, time) VALUES ('Football', 'Team A', 'Team B', 0, '881 Seventh Ave', 'Manhattan', 'NY', 'USA', 40.7658, -73.9799, '2023-10-28', '15:00')");
        db.execSQL("INSERT INTO Game (gameType, team1, team2, isJoined, street, city, state, country, latitude, longitude, date, time) VALUES ('Football', 'Team A', 'Team B', 0, '111-01 Roosevelt Ave', 'Queens', 'NY', 'USA', 40.7571, -73.8458, '2023-10-29', '16:00')");
    }
}