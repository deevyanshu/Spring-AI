package com.deevyanshu.HelpDesk.Repository;

import com.deevyanshu.HelpDesk.Entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TicketRepository extends JpaRepository<Ticket,Long> {
    Optional<Ticket> findByEmail(String email);
}
