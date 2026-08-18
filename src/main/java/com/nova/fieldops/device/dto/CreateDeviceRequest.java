package com.nova.fieldops.device.dto;

import com.nova.fieldops.device.DeviceStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateDeviceRequest(

        @NotBlank
        @Size(max = 100)
        String deviceCode,

        @NotBlank
        @Size(max = 150)
        String name,

        @NotNull
        Long siteId,

        @NotNull
        DeviceStatus status
) {
}