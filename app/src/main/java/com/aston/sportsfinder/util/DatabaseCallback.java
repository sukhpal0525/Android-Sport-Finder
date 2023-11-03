package com.aston.sportsfinder.util;

import androidx.annotation.NonNull;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

public class DatabaseCallback extends RoomDatabase.Callback {

    @Override
    public void onCreate(@NonNull SupportSQLiteDatabase db) {
        super.onCreate(db);

        db.execSQL("INSERT INTO Game (gameType, team1, team2, city, state, country, postcode, latitude, longitude, date, dateTime)"
                + "VALUES ('Football', 'Team A', 'Team B', 'Los Angeles', 'CA', 'USA', '90001', 34.0522, -118.2437, '2023-10-18', '15:00')");
    }
}