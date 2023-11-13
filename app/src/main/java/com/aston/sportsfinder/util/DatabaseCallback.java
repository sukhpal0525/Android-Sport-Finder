package com.aston.sportsfinder.util;

import androidx.annotation.NonNull;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.aston.sportsfinder.dao.GameDao;
import com.aston.sportsfinder.model.Game;

public class DatabaseCallback extends RoomDatabase.Callback {

    @Override
    public void onCreate(@NonNull SupportSQLiteDatabase db) {
        super.onCreate(db);
    }

    public void insertTestData(GameDao gameDao) {
        gameDao.insertGame(new Game(1, "Football", "Team A", "Team B", 2, 3, 0, "123 Broadway St", "Manhattan", "NY", "USA", 40.7831, -73.9712, "2023-10-20", "14:00"));
//
//
//
//        gameDao.insertGame(new Game(1, "Football", "Team A", "Team B", 0, "123 Broadway St", "Manhattan", "NY", "USA", 40.7831, -73.9712, "2023-10-20", "14:00"));
//        gameDao.insertGame(new Game(2, "Football", "Team A", "Team B", 0, "334 Furman St", "Brooklyn", "NY", "USA", 40.7006, -73.9946, "2023-10-21", "16:00"));
//        gameDao.insertGame(new Game(3, "Football", "Team A", "Team B", 0, "11101 Corona Ave", "Queens", "NY", "USA", 40.7462, -73.8458, "2023-10-22", "17:00"));
//        gameDao.insertGame(new Game(4, "Football", "Team A", "Team B", 0, "1 E 161 St", "The Bronx", "NY", "USA", 40.8296, -73.9262, "2023-10-23", "15:30"));
//        gameDao.insertGame(new Game(5, "Football", "Team A", "Team B", 0, "1410 Richmond Terrace", "Staten Island", "NY", "USA", 40.6404, -74.1357, "2023-10-24", "13:00"));
//        gameDao.insertGame(new Game(6, "Football", "Team A", "Team B", 0, "776 Lorimer St", "Brooklyn", "NY", "USA", 40.7215, -73.9523, "2023-10-26", "14:30"));
//        gameDao.insertGame(new Game(7, "Football", "Team A", "Team B", 0, "95 Prospect Park W", "Brooklyn", "NY", "USA", 40.6619, -73.9797, "2023-10-27", "10:00"));
//        gameDao.insertGame(new Game(8, "Football", "Team A", "Team B", 0, "1904 Surf Ave", "Brooklyn", "NY", "USA", 40.5753, -73.9797, "2023-10-28", "18:00"));
//        gameDao.insertGame(new Game(9, "Football", "Team A", "Team B", 0, "19 19th St", "Queens", "NY", "USA", 40.7798, -73.9223, "2023-10-29", "15:45"));
//        gameDao.insertGame(new Game(10, "Football", "Team A", "Team B", 0, "Highland Park", "Brooklyn", "NY", "USA", 40.6894, -73.8807, "2023-10-30", "16:00"));
    }
}

//      db.execSQL("INSERT INTO Game (gameType, team1, team2, isJoined, street, city, state, country, latitude, longitude, date, time) VALUES ('Football', 'Team A', 'Team B', 0, '456 Fulton St', 'Brooklyn', 'NY', 'USA', 40.6782, -73.9442, '2023-10-21', '16:00')");