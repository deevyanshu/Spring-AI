package com.deevyanshu.ToolCalling.Service;

import com.deevyanshu.ToolCalling.Tool.SimpleDateTimeTool;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class ChatService {

    private ChatClient chatClient;

    public ChatService(ChatClient chatClient)
    {
        this.chatClient=chatClient;
    }

    public String chat(String query)
    {
        return this.chatClient.prompt().tools(new SimpleDateTimeTool()).user(query).call().content();
    }
}
