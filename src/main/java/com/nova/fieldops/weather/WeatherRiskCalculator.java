package com.nova.fieldops.weather;

import com.nova.fieldops.ticket.WeatherRisk;
import org.springframework.stereotype.Component;

@Component
public class WeatherRiskCalculator {

    public WeatherRisk calculate(WeatherResponse weather) {

        if (weather.windSpeed() >= 80) {
            return WeatherRisk.EXTREME;
        }

        if (weather.windSpeed() >= 50) {
            return WeatherRisk.HIGH;
        }

        if (weather.windSpeed() >= 30) {
            return WeatherRisk.MODERATE;
        }

        return WeatherRisk.LOW;
    }
}