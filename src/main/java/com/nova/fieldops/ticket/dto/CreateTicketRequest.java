package com.nova.fieldops.ticket.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateTicketRequest(

        @NotBlank
        String title,

        @NotBlank
        String description,

        @NotNull
        Long deviceId
) {
}