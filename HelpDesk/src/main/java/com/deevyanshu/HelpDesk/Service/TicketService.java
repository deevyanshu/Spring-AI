package com.deevyanshu.HelpDesk.Service;

import com.deevyanshu.HelpDesk.Entity.Ticket;
import com.deevyanshu.HelpDesk.Repository.TicketRepository;
import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@Getter
@Setter
public class TicketService {

    private final TicketRepository ticketRepository;

    public TicketService(TicketRepository ticketRepository)
    {
        this.ticketRepository=ticketRepository;
    }

    @Transactional
    public Ticket createTicket(Ticket ticket)
    {
        ticket.setId(null);
        return this.ticketRepository.save(ticket);
    }

    public Ticket getTicket(Long id)
    {
        return this.ticketRepository.findById(id).get();
    }

    public Ticket getTicketByUsername(String username)
    {
        return this.ticketRepository.findByEmail(username).get();
    }

    public Ticket updateTicket(Ticket ticket)
    {
        return this.ticketRepository.save(ticket);
    }


}
