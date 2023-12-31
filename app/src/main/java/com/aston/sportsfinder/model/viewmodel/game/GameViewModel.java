package com.aston.sportsfinder.model.viewmodel.game;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.aston.sportsfinder.model.Game;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public class GameViewModel extends ViewModel {
    private MutableLiveData<List<Game>> gamesLiveData = new MutableLiveData<>();
    private final MutableLiveData<Game> selectedGame = new MutableLiveData<>();
    private final MutableLiveData<Game> newGameLiveData = new MutableLiveData<>();
    private final MutableLiveData<LatLng> selectedLocation = new MutableLiveData<>();
    private final MutableLiveData<Boolean> locationSelectionMode = new MutableLiveData<>(false);


    public void updateGames(List<Game> games) {
        gamesLiveData.setValue(games);
    }

    public LiveData<List<Game>> getGamesLiveData() {
        return gamesLiveData;
    }

    public void setSelectedGame(Game game) {
        selectedGame.setValue(game);
    }

    public LiveData<Game> getSelectedGame() {
        return selectedGame;
    }

    public void setNewGame(Game game) {
        newGameLiveData.postValue(game);
    }

    public void setSelectedLocation(LatLng latLng) {
        selectedLocation.setValue(latLng);
    }

    public LiveData<LatLng> getSelectedLocation() {
        return selectedLocation;
    }

    public void setLocationSelectionMode(boolean mode) {
        locationSelectionMode.postValue(mode);
    }

    public boolean isLocationSelectionMode() { return locationSelectionMode.getValue() != null && locationSelectionMode.getValue(); }
}