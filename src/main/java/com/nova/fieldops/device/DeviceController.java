package com.nova.fieldops.device;

import com.nova.fieldops.device.dto.CreateDeviceRequest;
import com.nova.fieldops.device.dto.DeviceResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/devices")
@RequiredArgsConstructor
public class DeviceController {

    private final DeviceService deviceService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'DISPATCHER')")
    public ResponseEntity<DeviceResponse> createDevice(
            @Valid @RequestBody CreateDeviceRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(deviceService.createDevice(request));
    }
}