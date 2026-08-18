package com.nova.fieldops.ticket;

import com.nova.fieldops.device.Device;
import com.nova.fieldops.device.DeviceRepository;
import com.nova.fieldops.ticket.calculation.CalculationEngine;
import com.nova.fieldops.ticket.calculation.CalculationResult;
import com.nova.fieldops.ticket.dto.CreateTicketRequest;
import com.nova.fieldops.ticket.dto.TicketResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketRepository ticketRepository;
    private final DeviceRepository deviceRepository;
    private final CalculationEngine calculationEngine;

    public TicketResponse createTicket(CreateTicketRequest request) {

        Device device = deviceRepository.findById(request.deviceId())
                .orElseThrow(() ->
                        new IllegalArgumentException("Device not found")
                );

        LocalDateTime now = LocalDateTime.now();

        CalculationResult calculation =
                calculationEngine.calculate(
                        WeatherRisk.LOW,
                        TicketPriority.MEDIUM
                );

        Ticket ticket = Ticket.builder()
                .title(request.title())
                .description(request.description())
                .device(device)
                .priority(calculation.priority())
                .status(TicketStatus.OPEN)
                .weatherRisk(calculation.weatherRisk())
                .slaDeadline(calculation.slaDeadline())
                .createdAt(now)
                .updatedAt(now)
                .build();

        Ticket savedTicket = ticketRepository.save(ticket);

        return toResponse(savedTicket);
    }

    private TicketResponse toResponse(Ticket ticket) {

        Long technicianId =
                ticket.getAssignedTechnician() == null
                        ? null
                        : ticket.getAssignedTechnician().getId();

        return new TicketResponse(
                ticket.getId(),
                ticket.getTitle(),
                ticket.getDescription(),
                ticket.getDevice().getId(),
                technicianId,
                ticket.getPriority(),
                ticket.getStatus(),
                ticket.getSlaDeadline(),
                ticket.getWeatherRisk(),
                ticket.getCreatedAt(),
                ticket.getUpdatedAt()
        );
    }
}