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
    public ResponseEntity<String> chatTemplate(@RequestParam(value = "q", required = true) String q,
                                               @RequestHeader(value = "userId") String userId)
    {
        return ResponseEntity.ok(chatService.chatTemplate(q,userId));
    }

    //it will stream the data
    @GetMapping(value = "/streamchat",produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public ResponseEntity<Flux<String>> streamChat(){
        return ResponseEntity.ok(chatService.streamChat());
    }
}
