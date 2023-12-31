package com.aston.sportsfinder.fragment.search;

import android.util.Log;
import android.widget.TextView;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.aston.sportsfinder.api.RetrofitClient;
import com.aston.sportsfinder.api.WeatherData;
import com.aston.sportsfinder.api.WeatherResponse;
import com.aston.sportsfinder.api.WeatherService;
import com.aston.sportsfinder.model.Game;

import org.apache.commons.text.WordUtils;

public class SearchViewModel extends ViewModel {

    private SearchRepository searchRepository;
    private static final String API_KEY = "ead55837c57cbeba862f405699d3b769";

    public SearchViewModel() {
        searchRepository = new SearchRepository();
    }

    public LiveData<WeatherResponse> fetchWeatherData(Game game) {
        return searchRepository.fetchWeatherData(game.getLatitude(), game.getLongitude(), API_KEY);
    }

    public void displayWeatherInfo(WeatherResponse weatherResponse, TextView tvWeatherInfo) {
        // Update text view with weather data
        if (weatherResponse != null && weatherResponse.getWeatherData() != null) {
            WeatherData weatherData = weatherResponse.getWeatherData().get(0);
            WeatherData.WeatherMain main = weatherData.getMain();
            WeatherData.WeatherDescription description = weatherData.getWeather().get(0);

            String weatherInfo =
                    main.getTemp() + "°C " + "(" + description.getMainDescription() + ")";
            tvWeatherInfo.setText(weatherInfo);
        } else {
            tvWeatherInfo.setText("Weather data not available.");
        }
    }

    public void displayDetailedWeatherInfo(WeatherResponse weatherResponse, TextView tvWeatherInfo) {
        // A bit more detailed weather data
        if (weatherResponse != null && weatherResponse.getWeatherData() != null) {
            WeatherData weatherData = weatherResponse.getWeatherData().get(0);
            WeatherData.WeatherMain main = weatherData.getMain();
            WeatherData.WeatherDescription description = weatherData.getWeather().get(0);

            String detailedWeatherInfo = main.getTemp() + "°C " +
                    "(" + description.getMainDescription() + ")" +
                    "\nDescription: " + WordUtils.capitalizeFully(description.getDescription());
            tvWeatherInfo.setText(detailedWeatherInfo);
        } else {
            tvWeatherInfo.setText("Weather data not available.");
        }
    }
}