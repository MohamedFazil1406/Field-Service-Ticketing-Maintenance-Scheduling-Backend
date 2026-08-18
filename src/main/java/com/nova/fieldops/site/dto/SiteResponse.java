package com.nova.fieldops.site.dto;

public record SiteResponse(
        Long id,
        String name,
        String address,
        Double latitude,
        Double longitude
) {
}