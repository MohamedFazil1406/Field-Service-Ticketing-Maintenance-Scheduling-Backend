package com.nova.fieldops.device.dto;

import com.nova.fieldops.device.DeviceStatus;

public record DeviceResponse(
        Long id,
        String deviceCode,
        String name,
        Long siteId,
        DeviceStatus status
) {
}