package com.deevyanshu.parsingResponse.Controller;

import com.deevyanshu.parsingResponse.Entity.Tut;
import com.deevyanshu.parsingResponse.Service.ChatService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1")
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @GetMapping("/chat")
    public ResponseEntity<Tut> chat(@RequestParam(value = "prompt", required = true) String prompt)
    {
        return ResponseEntity.ok(chatService.chat(prompt));
    }

    @GetMapping("/chatlist")
    public ResponseEntity<List<Tut>> chatList(@RequestParam(value = "prompt", required = true) String prompt)
    {
        return ResponseEntity.ok(chatService.chatList(prompt));
    }

    @GetMapping("/chattemplate")
    public ResponseEntity<String> chatTemplate()
    {
        return ResponseEntity.ok(chatService.chatTemplate());
    }
}
