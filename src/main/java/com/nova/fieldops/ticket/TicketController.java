package com.nova.fieldops.ticket;

import com.nova.fieldops.ticket.dto.CreateTicketRequest;
import com.nova.fieldops.ticket.dto.TicketResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tickets")
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'DISPATCHER', 'TECHNICIAN')")
    public ResponseEntity<TicketResponse> createTicket(
            @Valid @RequestBody CreateTicketRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ticketService.createTicket(request));
    }
}