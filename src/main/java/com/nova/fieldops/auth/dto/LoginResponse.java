package com.nova.fieldops.auth.dto;

public record LoginResponse(
        String token,
        String tokenType
) {
}