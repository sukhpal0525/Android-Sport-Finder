package com.aston.sportsfinder;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.aston.sportsfinder.dao.GameDao;
import com.aston.sportsfinder.model.Game;

@Database(entities = {Game.class}, version = 5)
public abstract class AppDatabase extends RoomDatabase {

    public abstract GameDao gameDao();
}