package com.aston.sportsfinder.fragment.search;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.aston.sportsfinder.api.RetrofitClient;
import com.aston.sportsfinder.api.WeatherResponse;
import com.aston.sportsfinder.api.WeatherService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchRepository {

    private final WeatherService weatherService;

    public SearchRepository() {
        weatherService = RetrofitClient.getRetrofitInstance().create(WeatherService.class);
    }

    public LiveData<WeatherResponse> fetchWeatherData(double latitude, double longitude, String apiKey) {
        MutableLiveData<WeatherResponse> liveData = new MutableLiveData<>();

        Call<WeatherResponse> call = weatherService.getWeatherForecast(latitude, longitude, apiKey, "metric");
        call.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                if (response.isSuccessful()) {
                    liveData.setValue(response.body());
                } else {
                    liveData.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                liveData.setValue(null);
            }
        });
        return liveData;
    }
}