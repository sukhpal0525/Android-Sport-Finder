package com.aston.sportsfinder.api;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class WeatherData implements Serializable {

    @SerializedName("dt")
    private long timestamp;

    @SerializedName("main")
    private WeatherMain mainInfo;

    @SerializedName("weather")
    private List<WeatherDescription> weather;

    public static class WeatherMain {
        @SerializedName("temp")
        private double temp;

        public double getTemp() {
            return temp;
        }
    }

    public static class WeatherDescription {
        @SerializedName("main")
        private String mainDescription;
        @SerializedName("description")
        private String description;

        public String getMainDescription() {
            return mainDescription;
        }

        public String getDescription() {
            return description;
        }
    }

    public WeatherMain getMain() {
        return mainInfo;
    }

    public List<WeatherDescription> getWeather() {
        return weather;
    }
}