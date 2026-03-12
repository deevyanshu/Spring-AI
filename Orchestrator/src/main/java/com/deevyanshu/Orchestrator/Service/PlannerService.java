package com.deevyanshu.Orchestrator.Service;

import com.deevyanshu.Orchestrator.Dto.Workers;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.List;

@Service
public class PlannerService {

    private ChatClient chatClient;

    public PlannerService(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    public List<Workers> plan(String code)
    {
        System.out.println("planning");
        return this.chatClient.prompt().system("You are a task planner and you have to plan the task for the given code the task planned should be: analyze, testcases, review. Also include the prompts for each task")
                .user(code).call().entity(new ParameterizedTypeReference<List<Workers>>() {});
    }
}
