package com.nova.fieldops.weather;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class WeatherService {

    private final RestClient restClient;

    public WeatherService(RestClient.Builder builder) {
        this.restClient = builder
                .baseUrl("https://api.open-meteo.com")
                .build();
    }

    public WeatherResponse getWeather(
            double latitude,
            double longitude
    ) {

        OpenMeteoResponse response = restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/v1/forecast")
                        .queryParam("latitude", latitude)
                        .queryParam("longitude", longitude)
                        .queryParam(
                                "current",
                                "temperature_2m,wind_speed_10m,weather_code"
                        )
                        .build())
                .retrieve()
                .body(OpenMeteoResponse.class);

        if (response == null || response.current() == null) {
            throw new IllegalStateException(
                    "Unable to retrieve weather data"
            );
        }

        return new WeatherResponse(
                response.current().temperature(),
                response.current().windSpeed(),
                response.current().weatherCode()
        );
    }
}