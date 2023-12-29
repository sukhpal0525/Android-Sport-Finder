package com.aston.sportsfinder.util;

import androidx.annotation.NonNull;
import androidx.room.PrimaryKey;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.aston.sportsfinder.dao.GameDao;
import com.aston.sportsfinder.dao.NotificationDao;
import com.aston.sportsfinder.dao.UserDao;
import com.aston.sportsfinder.model.Game;
import com.aston.sportsfinder.model.Notification;
import com.aston.sportsfinder.model.User;

import java.util.concurrent.ExecutorService;

public class DatabaseCallback extends RoomDatabase.Callback {

    private ExecutorService asyncTaskExecutor;

    public DatabaseCallback(ExecutorService executorService) {
        this.asyncTaskExecutor = executorService;
    }

    @Override
    public void onCreate(@NonNull SupportSQLiteDatabase db) {
        super.onCreate(db);
    }

    public void insertTestData(GameDao gameDao, UserDao userDao, NotificationDao notificationDao) {
        asyncTaskExecutor.execute(() -> {
            if (userDao.countUsers() == 0) {
                userDao.insertUser(new User(1, "FName", "LName"));
            }
            if (gameDao.countGames() == 0) {
                gameDao.insertGame(new Game("Cricket", "Spain", "Poland", 4, 2, false, false, "County Ground", "Northampton", "Northamptonshire", "England", 52.2405, -0.9032, "2023-10-27", "10:00", "Sukhpal", 22, 18, "Beginner", "Cricket bats, Balls, Protective gear", "3 hours", 0.00, "Beginners welcome"));

                gameDao.insertGame(new Game("Football", "Arsenal", "Newcastle", 1, 0, false, false, "Hyde Park", "London", "Greater London", "England", 51.507268, -0.165730, "2023-10-20", "14:00", "Sukhpal", 22, 20, "Intermediate", "Football, Jerseys, Shin Guards", "90 minutes", 0.00, "Standard football rules apply"));
                gameDao.insertGame(new Game("Football", "England", "Newcastle", 2, 1, false, true, "Heaton Park", "Manchester", "Greater Manchester", "England", 53.533013, -2.262634, "2023-11-10", "12:00", "Sukhpal", 22, 22, "Intermediate", "Football, Jerseys, Shin Guards", "90 minutes", 0.00, "Standard football rules apply"));
                gameDao.insertGame(new Game("Football", "Liverpool", "Barcelona", 3, 5, false, false, "Victoria Park", "Bath", "Somerset", "England", 51.3848, -2.3615, "2023-10-21", "16:00", "Sukhpal", 22, 20, "Intermediate", "Football, Jerseys, Shin Guards", "90 minutes", 0.00, "Standard football rules apply"));

                gameDao.insertGame(new Game("Baseball", "England", "France", 3, 1, false, false, "Roundhay Park", "Leeds", "West Yorkshire", "England", 53.8375, -1.4994, "2023-10-24", "13:00", "Sukhpal", 18, 15, "Beginner", "Bats, Gloves, Helmets", "2 hours", 0.00, "Standard baseball rules apply"));
                gameDao.insertGame(new Game("Baseball", "Germany", "Russia", 3, 2, false, false, "Cannon Hill Park", "Birmingham", "West Midlands", "England", 52.4521, -1.8923, "2023-10-26", "14:30", "Sukhpal", 18, 18, "Intermediate", "Bats, Gloves, Helmets", "2 hours", 0.00, "Standard baseball rules apply"));

                gameDao.insertGame(new Game("Rugby", "USA", "France", 1, 4, false, false, "Eaton Park", "Norwich", "Norfolk", "England", 52.6308859, 1.297355, "2023-10-24", "13:00", "Sukhpal", 30, 25, "Intermediate", "Rugby ball, Cleats, Protective gear", "80 minutes", 0.00, "Standard rugby rules apply"));
                gameDao.insertGame(new Game("Rugby", "PSG", "Valencia", 1, 3, false, false, "Sandy Park", "Exeter", "Devon", "England", 50.7256, -3.4717, "2023-11-07", "18:30", "Sukhpal", 30, 30, "Advanced", "Rugby ball, Cleats, Protective gear", "80 minutes", 0.00, "Competitive level"));
                //gameDao.insertGame(new Game("Rugby", "Italy", "France", 1, 3, false, false, "Sandy Park", "Exeter", "Devon", "England", 50.7256, -3.4717, "2023-11-07", "18:30", "Sukhpal", 30, 30, "Advanced", "Rugby ball, Cleats, Protective gear", "80 minutes", "Adult", 0.00, "Competitive level, standard rules"));

                gameDao.insertGame(new Game("Hockey", "USA", "France", 1, 4, false, false, "Bute Park", "Cardiff", "Wales", "UK", 51.4841, -3.1869, "2023-10-24", "13:00", "Sukhpal", 30, 25, "Intermediate", "Hockey sticks, Pucks, Protective gear", "60 minutes", 0.00, "Standard hockey rules"));

                gameDao.insertGame(new Game("Tennis", "Japan", "France", 1, 1, false, false, "Mill Road", "Cambridge", "Cambridgeshire", "England", 52.1989, 0.1374, "2023-10-27", "10:00", "Sukhpal",4, 4, "Intermediate","Tennis rackets, Tennis balls","1-2 hours", 0.00,"Standard tennis rules, friendly match"));
                //                gameDao.insertGame(new Game("Cricket", "Spain", "Poland", 1, 3, false, false, "Flushing Meadows Corona Park", "Queens", "NY", "USA", 40.7410, -73.8410, "2023-10-27", "10:00"));
            }
        });
    }
}

//        gameDao.insertGame(new Game("Football", "Arsenal", "Liverpool", 2, 3, false, false, "Prospect Park", "Brooklyn", "NY", "USA", 40.6602, -73.9690, "2023-11-05", "17:00"));
//        gameDao.insertGame(new Game("Football", "Juventus", "Marseille", 2, 3, false, false, "Clove Lakes Park", "Staten Island", "NY", "USA", 40.6150, -74.1058, "2023-11-08", "16:00"));
//        gameDao.insertGame(new Game("Football", "AC Milan", "USA", 2, 3, false, false, "Van Cortlandt Park", "The Bronx", "NY", "USA", 40.8985, -73.8867, "2023-11-09", "14:00"));


// gameType, team1, team2, score1, score2, isJoined, isStarted, street, city, state, country, latitude, longitude, date, time

//      gameDao.insertGame(new Game(4, "Football", "Team A", "Team B", 2, 3, false, true, "1 E 161 St", "The Bronx", "NY", "USA", 40.8296, -73.9262, "2023-10-23", "15:30"));
//      gameDao.insertGame(new Game(5, "Football", "Team A", "Team B", 2, 3, false, true, "1410 Richmond Terrace", "Staten Island", "NY", "USA", 40.6404, -74.1357, "2023-10-24", "13:00"));
//      gameDao.insertGame(new Game(6, "Football", "Team A", "Team B", 2, 3, false, true, "776 Lorimer St", "Brooklyn", "NY", "USA", 40.7215, -73.9523, "2023-10-26", "14:30"));
//      gameDao.insertGame(new Game(7, "Football", "Team A", "Team B", 2, 3, false, true, "95 Prospect Park W", "Brooklyn", "NY", "USA", 40.6619, -73.9797, "2023-10-27", "10:00"));
//      gameDao.insertGame(new Game(8, "Football", "Team A", "Team B", 2, 3, false, true, "1904 Surf Ave", "Brooklyn", "NY", "USA", 40.5753, -73.9797, "2023-10-28", "18:00"));
//      gameDao.insertGame(new Game(9, "Football", "Team A", "Team B", 2, 3, false, true, "19 19th St", "Queens", "NY", "USA", 40.7798, -73.9223, "2023-10-29", "15:45"));
//      gameDao.insertGame(new Game(10, "Football", "Team A", "Team B", 2, 3, false, true, "Highland Park", "Brooklyn", "NY", "USA", 40.6894, -73.8807, "2023-10-30", "16:00"));

//      db.execSQL("INSERT INTO Game (gameType, team1, team2, isJoined, street, city, state, country, latitude, longitude, date, time) VALUES ('Football', 'Team A', 'Team B', 0, '456 Fulton St', 'Brooklyn', 'NY', 'USA', 40.6782, -73.9442, '2023-10-21', '16:00')");