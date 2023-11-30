package com.aston.sportsfinder.api;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WeatherResponse {

    @SerializedName("list")
    private List<WeatherData> weatherData;

    public List<WeatherData> getWeatherData() {
        return weatherData;
    }
}