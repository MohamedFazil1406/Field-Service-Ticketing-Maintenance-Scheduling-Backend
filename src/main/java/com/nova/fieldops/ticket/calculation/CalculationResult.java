package com.nova.fieldops.ticket.calculation;

import com.nova.fieldops.ticket.TicketPriority;
import com.nova.fieldops.ticket.WeatherRisk;

import java.time.LocalDateTime;

public record CalculationResult(
        TicketPriority priority,
        WeatherRisk weatherRisk,
        LocalDateTime slaDeadline
) {
}