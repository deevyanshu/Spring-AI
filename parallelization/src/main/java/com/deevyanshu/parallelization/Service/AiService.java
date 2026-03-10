package com.deevyanshu.parallelization.Service;

import com.deevyanshu.parallelization.Dto.AnalyzeResponse;
import com.deevyanshu.parallelization.Dto.Response;
import com.deevyanshu.parallelization.Dto.Task;
import com.deevyanshu.parallelization.Dto.TestcaseResponse;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;


@Service
public class AiService {

    private ChatClient chatClient;

    @Qualifier("ai-executor")
    private Executor executor;


    public AiService(ChatClient chatClient, Executor executor) {
        this.chatClient = chatClient;
        this.executor = executor;
    }

    public Response process(String query)
    {
//        List<Task> tasks=List.of(new Task("Analyze"),new Task("testcase"));
//
//        List<CompletableFuture<Void>> futures=tasks.stream().map(task->CompletableFuture.supplyAsync(()->
//            runtask(task,query)
//        ,executor)).collect(Collectors.toList());
//
//        //wait for all futures to complete
//        CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new)).join();
//
//        System.out.println("generating response");
//        return response;

        CompletableFuture<String> summaryFuture = CompletableFuture.supplyAsync(() -> {
            return this.chatClient.prompt()
                    .system("summarize code in 2 lines")
                    .user(query)
                    .call()
                    .entity(AnalyzeResponse.class)
                    .Analyze();
        }, executor);

        CompletableFuture<List<String>> testcaseFuture = CompletableFuture.supplyAsync(() -> {
            return this.chatClient.prompt()
                    .system("provide 3 testcases for this code in json format")
                    .user(query)
                    .call()
                    .entity(TestcaseResponse.class)
                    .testcase();
        }, executor);

        // 2. Combine them without a manual loop
        return CompletableFuture.allOf(summaryFuture, testcaseFuture)
                .thenApply(v -> {
                    String summary = summaryFuture.join();
                    List<String> testcases = testcaseFuture.join();
                    return new Response(summary, testcases);
                })
                .join();
    }
}
