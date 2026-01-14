package com.deevyanshu.advisor.Controller;

import com.deevyanshu.advisor.Service.ChatService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1")
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }
    @GetMapping("/chattemplate")
    public ResponseEntity<String> chatTemplate()
    {
        return ResponseEntity.ok(chatService.chatTemplate());
    }
}
