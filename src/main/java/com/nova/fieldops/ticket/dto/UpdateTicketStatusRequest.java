package com.nova.fieldops.ticket.dto;

import com.nova.fieldops.ticket.TicketStatus;
import jakarta.validation.constraints.NotNull;

public record UpdateTicketStatusRequest(

        @NotNull
        TicketStatus status
) {
}