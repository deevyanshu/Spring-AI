package com.deevyanshu.Orchestrator.Service;

import com.deevyanshu.Orchestrator.Dto.WorkerResponse;
import com.deevyanshu.Orchestrator.Dto.Workers;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

@Service
public class PlanExecutorService {

    private ChatClient chatClient;

    @Qualifier("Ai-executor")
    private Executor executor;

    public PlanExecutorService(ChatClient chatClient, Executor executor) {
        this.chatClient = chatClient;
        this.executor = executor;
    }

    public List<WorkerResponse> execute(List<Workers> workers,String code){
        System.out.println("executing");
        List<CompletableFuture<WorkerResponse>> futures=workers.stream().map(worker->CompletableFuture.supplyAsync(
                ()->{
                    System.out.println("entered");
                    return this.chatClient.prompt().system(worker.prompt()+": "+worker.task()).user(code).call()
                            .entity(WorkerResponse.class);
                }
        ,executor)).collect(Collectors.toList());

        CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new)).join();

        List<WorkerResponse> finalResponse=futures.stream().map(CompletableFuture::join).collect(Collectors.toList());

        List<WorkerResponse> response=new ArrayList<>();

        for(WorkerResponse res:finalResponse)
        {
            response.add(new WorkerResponse(res.task(),res.response()));
        }

        return response;
    }

}
