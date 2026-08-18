package com.nova.fieldops.weather;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CurrentWeather(

        @JsonProperty("temperature_2m")
        double temperature,

        @JsonProperty("wind_speed_10m")
        double windSpeed,

        @JsonProperty("weather_code")
        int weatherCode
) {
}