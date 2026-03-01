package com.deevyanshu.HelpDesk.Service;

import com.deevyanshu.HelpDesk.Tools.TicketDatabaseTool;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Getter
@Setter
public class AiService {

    private final ChatClient chatClient;

    private final TicketDatabaseTool ticketDatabaseTool;

    @Value("classpath:/helpdesk_system.st")
    private Resource systemResource;

    public String getResponseFromAssistant(String query,String conversationId)
    {
        return this.chatClient.prompt().advisors(advisorspec->advisorspec
                .param(ChatMemory.CONVERSATION_ID,conversationId))
                .tools(ticketDatabaseTool).system(systemResource).user(query).call().content();
    }
}
