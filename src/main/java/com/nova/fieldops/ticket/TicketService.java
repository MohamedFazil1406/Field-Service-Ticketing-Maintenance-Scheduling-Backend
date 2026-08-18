package com.nova.fieldops.ticket;

import com.nova.fieldops.common.BadRequestException;
import com.nova.fieldops.common.ResourceNotFoundException;
import com.nova.fieldops.device.Device;
import com.nova.fieldops.device.DeviceRepository;
import com.nova.fieldops.ticket.calculation.CalculationEngine;
import com.nova.fieldops.ticket.calculation.CalculationResult;
import com.nova.fieldops.ticket.dto.AssignTechnicianRequest;
import com.nova.fieldops.ticket.dto.CreateTicketRequest;
import com.nova.fieldops.ticket.dto.TicketResponse;
import com.nova.fieldops.ticket.dto.UpdateTicketStatusRequest;
import com.nova.fieldops.user.User;
import com.nova.fieldops.user.UserRepository;
import com.nova.fieldops.user.UserRole;
import com.nova.fieldops.weather.WeatherResponse;
import com.nova.fieldops.weather.WeatherRiskCalculator;
import com.nova.fieldops.weather.WeatherService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketRepository ticketRepository;
    private final DeviceRepository deviceRepository;
    private final CalculationEngine calculationEngine;
    private final UserRepository userRepository;
    private final WeatherService weatherService;
    private final WeatherRiskCalculator weatherRiskCalculator;

    public TicketResponse createTicket(CreateTicketRequest request) {

        Device device = deviceRepository.findByIdWithSite(request.deviceId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Device not found: " + request.deviceId()
                        )
                );

        if (device.getSite() == null) {
            throw new BadRequestException(
                    "Device is not associated with a site"
            );
        }

        if (device.getSite().getLatitude() == null ||
                device.getSite().getLongitude() == null) {

            throw new BadRequestException(
                    "Site does not have valid coordinates"
            );
        }

        LocalDateTime now = LocalDateTime.now();

        WeatherResponse weather = weatherService.getWeather(
                device.getSite().getLatitude(),
                device.getSite().getLongitude()
        );

        WeatherRisk weatherRisk =
                weatherRiskCalculator.calculate(weather);

        CalculationResult calculation =
                calculationEngine.calculate(
                        weatherRisk,
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

    public TicketResponse assignTechnician(
            Long ticketId,
            AssignTechnicianRequest request
    ) {

        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Ticket not found: " + ticketId
                        )
                );

        User technician = userRepository.findById(
                        request.technicianId()
                )
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Technician not found: "
                                        + request.technicianId()
                        )
                );

        if (technician.getRole() != UserRole.TECHNICIAN) {
            throw new BadRequestException(
                    "Selected user is not a technician"
            );
        }

        ticket.setAssignedTechnician(technician);
        ticket.setStatus(TicketStatus.ASSIGNED);
        ticket.setUpdatedAt(LocalDateTime.now());

        Ticket savedTicket = ticketRepository.save(ticket);

        return toResponse(savedTicket);
    }

    public TicketResponse updateStatus(
            Long ticketId,
            UpdateTicketStatusRequest request,
            Authentication authentication
    ) {

        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Ticket not found: " + ticketId
                        )
                );

        String currentUserEmail = authentication.getName();

        User currentUser = userRepository.findByEmail(
                        currentUserEmail
                )
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Authenticated user not found"
                        )
                );

        if (currentUser.getRole() == UserRole.TECHNICIAN) {

            if (ticket.getAssignedTechnician() == null) {
                throw new AccessDeniedException(
                        "Ticket is not assigned to a technician"
                );
            }

            if (!ticket.getAssignedTechnician()
                    .getId()
                    .equals(currentUser.getId())) {

                throw new AccessDeniedException(
                        "You can only update tickets assigned to you"
                );
            }
        }

        TicketStatus currentStatus = ticket.getStatus();
        TicketStatus newStatus = request.status();

        validateTransition(currentStatus, newStatus);

        ticket.setStatus(newStatus);
        ticket.setUpdatedAt(LocalDateTime.now());

        Ticket savedTicket = ticketRepository.save(ticket);

        return toResponse(savedTicket);
    }

    private void validateTransition(
            TicketStatus currentStatus,
            TicketStatus newStatus
    ) {

        boolean valid = switch (currentStatus) {

            case OPEN ->
                    newStatus == TicketStatus.ASSIGNED;

            case ASSIGNED ->
                    newStatus == TicketStatus.IN_PROGRESS;

            case IN_PROGRESS ->
                    newStatus == TicketStatus.RESOLVED;

            case RESOLVED ->
                    newStatus == TicketStatus.CLOSED;

            case CLOSED ->
                    false;
        };

        if (!valid) {
            throw new BadRequestException(
                    "Invalid ticket status transition: "
                            + currentStatus
                            + " -> "
                            + newStatus
            );
        }
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