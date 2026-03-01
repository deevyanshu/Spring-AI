package com.deevyanshu.HelpDesk.Controller;

import com.deevyanshu.HelpDesk.Service.AiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class AiController {

    private final AiService aiService;

    @PostMapping("/chat")
    public ResponseEntity<String> getResponse(@RequestBody String query, @RequestHeader("ConversationId")String conversationId)
    {
        return ResponseEntity.ok(aiService.getResponseFromAssistant(query,conversationId));
    }
}
