package com.deevyanshu.HelpDesk.Tools;

import com.deevyanshu.HelpDesk.Entity.Ticket;
import com.deevyanshu.HelpDesk.Service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TicketDatabaseTool {

    private final TicketService ticketService;

    @Tool(description = "This tool helps to create new ticket in database")
    public Ticket createTicketTool(@ToolParam(description = "Ticket fields required to create new ticket") Ticket ticket)
    {
        return ticketService.createTicket(ticket);
    }

    @Tool(description = "this tool helps to get ticket by username")
    public Ticket getTicketByUsername(@ToolParam(description = "email whose ticket is required") String email)
    {
        return ticketService.getTicketByUsername(email);
    }

    @Tool(description = "this tool helps to update the ticket")
    public Ticket updateTicketBy(@ToolParam(description = "new ticket fields required to update with ticket id") Ticket ticket)
    {
        return ticketService.updateTicket(ticket);
    }

}
