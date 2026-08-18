package com.nova.fieldops.ticket;

import com.nova.fieldops.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

    List<Ticket> findByAssignedTechnician(User technician);

    List<Ticket> findByStatus(TicketStatus status);
}