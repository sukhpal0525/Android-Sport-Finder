package com.aston.sportsfinder.model.viewmodel.game;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.aston.sportsfinder.model.Game;

import java.util.List;

public class GameViewModel extends ViewModel {
    private MutableLiveData<List<Game>> gamesLiveData = new MutableLiveData<>();

    public void updateGames(List<Game> games) {
        gamesLiveData.setValue(games);
    }

    public LiveData<List<Game>> getGamesLiveData() {
        return gamesLiveData;
    }
}