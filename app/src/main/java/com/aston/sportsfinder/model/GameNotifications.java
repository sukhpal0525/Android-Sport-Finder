package com.aston.sportsfinder.model;

import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Relation;

import java.util.List;

public class GameNotifications {

    @Embedded
    public Game game;

    @Relation(
            parentColumn = "id",
            entityColumn = "gameId"
    )

    public List<Notification> notifications;
}