package com.nova.fieldops.ticket;

import com.nova.fieldops.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

    long countByAssignedTechnicianAndStatusIn(
            User technician,
            Iterable<TicketStatus> statuses
    );

    long countByStatusAndSlaDeadlineBefore(
            TicketStatus status,
            LocalDateTime deadline
    );
}