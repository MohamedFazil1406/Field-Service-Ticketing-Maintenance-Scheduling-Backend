package com.nova.fieldops.ticket.dto;

import com.nova.fieldops.ticket.TicketPriority;
import com.nova.fieldops.ticket.TicketStatus;
import com.nova.fieldops.ticket.WeatherRisk;

import java.time.LocalDateTime;

public record TicketResponse(

        Long id,

        String title,

        String description,

        Long deviceId,

        Long assignedTechnicianId,

        TicketPriority priority,

        TicketStatus status,

        LocalDateTime slaDeadline,

        WeatherRisk weatherRisk,

        LocalDateTime createdAt,

        LocalDateTime updatedAt
) {
}