package com.nova.fieldops.weather;

public record WeatherResponse(
        double temperature,
        double windSpeed,
        int weatherCode
) {
}