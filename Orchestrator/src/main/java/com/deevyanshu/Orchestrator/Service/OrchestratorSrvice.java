package com.deevyanshu.Orchestrator.Service;

import com.deevyanshu.Orchestrator.Dto.WorkerResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrchestratorSrvice {

    private PlannerService plannerService;
    private PlanExecutorService executorService;

    public OrchestratorSrvice(PlannerService plannerService, PlanExecutorService executorService) {
        this.plannerService = plannerService;
        this.executorService = executorService;
    }

    public List<WorkerResponse> orchestrate(String code)
    {
        return executorService.execute(plannerService.plan(code),code);
    }
}
