package com.nova.fieldops.device;

import com.nova.fieldops.common.BadRequestException;
import com.nova.fieldops.common.ResourceNotFoundException;
import com.nova.fieldops.device.dto.CreateDeviceRequest;
import com.nova.fieldops.device.dto.DeviceResponse;
import com.nova.fieldops.site.Site;
import com.nova.fieldops.site.SiteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class DeviceService {

    private final DeviceRepository deviceRepository;
    private final SiteRepository siteRepository;

    public DeviceResponse createDevice(CreateDeviceRequest request) {

        if (deviceRepository.findByDeviceCode(request.deviceCode()).isPresent()) {
            throw new BadRequestException(
                    "Device with this code already exists"
            );
        }

        Site site = siteRepository.findById(request.siteId())
                .orElseThrow(() ->
                        new IllegalArgumentException("Site not found")
                );

        Device device = Device.builder()
                .deviceCode(request.deviceCode())
                .name(request.name())
                .site(site)
                .status(request.status())
                .createdAt(LocalDateTime.now())
                .build();

        Device savedDevice = deviceRepository.save(device);

        return new DeviceResponse(
                savedDevice.getId(),
                savedDevice.getDeviceCode(),
                savedDevice.getName(),
                savedDevice.getSite().getId(),
                savedDevice.getStatus()
        );
    }
}