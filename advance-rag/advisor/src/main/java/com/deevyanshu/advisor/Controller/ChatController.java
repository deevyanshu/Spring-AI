package com.deevyanshu.advisor.Controller;

import com.deevyanshu.advisor.Service.ChatService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("api/v1")
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }
    @GetMapping("/chattemplate")
    public ResponseEntity<String> chatTemplate(@RequestParam(value = "q", required = true) String q)
    {
        return ResponseEntity.ok(chatService.chatTemplate(q));
    }

    //it will stream the data
   }
