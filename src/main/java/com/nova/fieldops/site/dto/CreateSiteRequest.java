package com.nova.fieldops.site.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateSiteRequest(

        @NotBlank
        String name,

        @NotBlank
        String address,

        @NotNull
        Double latitude,

        @NotNull
        Double longitude
) {
}