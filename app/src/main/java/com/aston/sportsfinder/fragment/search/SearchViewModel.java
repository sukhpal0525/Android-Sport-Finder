package com.aston.sportsfinder.fragment.search;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.aston.sportsfinder.api.RetrofitClient;
import com.aston.sportsfinder.api.WeatherResponse;
import com.aston.sportsfinder.api.WeatherService;
import com.aston.sportsfinder.model.Game;

public class SearchViewModel extends ViewModel {

    private SearchRepository searchRepository;
    private static final String API_KEY = "dc30793e6a621969d4560be369d8cef1";

    public SearchViewModel() {
        searchRepository = new SearchRepository();
    }

    public LiveData<WeatherResponse> fetchWeatherData(Game game) {
        return searchRepository.fetchWeatherData(game.getLatitude(), game.getLongitude(), API_KEY);
    }
}