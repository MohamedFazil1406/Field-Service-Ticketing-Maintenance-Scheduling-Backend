package com.nova.fieldops.user.dto;

import com.nova.fieldops.user.UserRole;

public record UserResponse(
        Long id,
        String name,
        String email,
        UserRole role
) {
}