package com.deevyanshu.parallelization.Controller;

import com.deevyanshu.parallelization.Dto.Request;
import com.deevyanshu.parallelization.Dto.Response;
import com.deevyanshu.parallelization.Service.AiService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1")
public class ParallelController {

    private AiService aiService;

    public ParallelController(AiService aiService) {
        this.aiService = aiService;
    }

    @PostMapping("/chat")
    public ResponseEntity<Response> get(@RequestBody Request req)
    {
        return ResponseEntity.ok(aiService.process(req.getCode()));
    }
}
