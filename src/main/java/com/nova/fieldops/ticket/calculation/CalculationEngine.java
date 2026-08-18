package com.nova.fieldops.ticket.calculation;

import com.nova.fieldops.ticket.TicketPriority;
import com.nova.fieldops.ticket.WeatherRisk;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class CalculationEngine {

    public CalculationResult calculate(
            WeatherRisk weatherRisk,
            TicketPriority requestedPriority
    ) {

        TicketPriority priority = calculatePriority(
                weatherRisk,
                requestedPriority
        );

        LocalDateTime slaDeadline = calculateSla(
                priority,
                weatherRisk
        );

        return new CalculationResult(
                priority,
                weatherRisk,
                slaDeadline
        );
    }

    private TicketPriority calculatePriority(
            WeatherRisk weatherRisk,
            TicketPriority requestedPriority
    ) {

        if (weatherRisk == WeatherRisk.EXTREME) {
            return TicketPriority.CRITICAL;
        }

        if (weatherRisk == WeatherRisk.HIGH &&
                requestedPriority == TicketPriority.LOW) {
            return TicketPriority.MEDIUM;
        }

        return requestedPriority;
    }

    private LocalDateTime calculateSla(
            TicketPriority priority,
            WeatherRisk weatherRisk
    ) {

        int hours;

        switch (priority) {
            case CRITICAL -> hours = 4;
            case HIGH -> hours = 8;
            case MEDIUM -> hours = 24;
            case LOW -> hours = 48;
            default -> hours = 24;
        }

        if (weatherRisk == WeatherRisk.EXTREME) {
            hours = Math.min(hours, 4);
        }

        return LocalDateTime.now().plusHours(hours);
    }
}