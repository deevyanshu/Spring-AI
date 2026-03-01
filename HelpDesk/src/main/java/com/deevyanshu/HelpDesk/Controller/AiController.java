package com.deevyanshu.HelpDesk.Controller;

import com.deevyanshu.HelpDesk.Service.AiService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1")
public class AiController {

    private final AiService aiService;

    public AiController(AiService aiService)
    {
        this.aiService=aiService;
    }

    @PostMapping("/chat")
    public ResponseEntity<String> getResponse(@RequestBody String query, @RequestHeader("ConversationId")String conversationId)
    {
        return ResponseEntity.ok(aiService.getResponseFromAssistant(query,conversationId));
    }

    @PostMapping(value = "/chat-stream")
    public Flux<String> getResponseStream(@RequestBody String query, @RequestHeader("ConversationId")String conversationId)
    {
        return aiService.streamResponseFromAssistant(query,conversationId);
    }
}
