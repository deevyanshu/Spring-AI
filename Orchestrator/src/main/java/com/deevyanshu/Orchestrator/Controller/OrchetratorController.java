package com.deevyanshu.Orchestrator.Controller;

import com.deevyanshu.Orchestrator.Dto.WorkerResponse;
import com.deevyanshu.Orchestrator.Service.OrchestratorSrvice;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1")
public class OrchetratorController {

    private OrchestratorSrvice orchestratorSrvice;

    public OrchetratorController(OrchestratorSrvice orchestratorSrvice) {
        this.orchestratorSrvice = orchestratorSrvice;
    }

    @PostMapping("/chat")
    public ResponseEntity<List<WorkerResponse>> get(@RequestBody String q)
    {
        return ResponseEntity.ok(orchestratorSrvice.orchestrate(q));
    }
}
