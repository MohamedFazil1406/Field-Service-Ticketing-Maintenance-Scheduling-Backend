package com.nova.fieldops.device.dto;

import com.nova.fieldops.device.DeviceStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateDeviceRequest(

        @NotBlank
        String deviceCode,

        @NotBlank
        String name,

        @NotNull
        Long siteId,

        @NotNull
        DeviceStatus status
) {
}