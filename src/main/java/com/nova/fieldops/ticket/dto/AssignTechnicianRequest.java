package com.nova.fieldops.ticket.dto;

import jakarta.validation.constraints.NotNull;

public record AssignTechnicianRequest(
        @NotNull
        Long technicianId
) {
}