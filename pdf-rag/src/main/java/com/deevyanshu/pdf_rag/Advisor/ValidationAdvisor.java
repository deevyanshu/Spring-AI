package com.deevyanshu.pdf_rag.Advisor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.api.CallAdvisor;
import org.springframework.ai.chat.client.advisor.api.CallAdvisorChain;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.evaluation.FactCheckingEvaluator;
import org.springframework.ai.chat.evaluation.RelevancyEvaluator;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.content.Content;
import org.springframework.ai.document.Document;
import org.springframework.ai.evaluation.EvaluationRequest;

import java.util.List;

public class ValidationAdvisor implements CallAdvisor {

    private final RelevancyEvaluator relevancyEvaluator;

    private Logger logger= LoggerFactory.getLogger(ValidationAdvisor.class);

    public ValidationAdvisor(ChatClient.Builder builder) {
        this.relevancyEvaluator = new RelevancyEvaluator(builder);
    }

    @Override
    public ChatClientResponse adviseCall(ChatClientRequest chatClientRequest, CallAdvisorChain callAdvisorChain) {
        ChatClientResponse response=callAdvisorChain.nextCall(chatClientRequest);
        String usertext=chatClientRequest.prompt().getContents();
        logger.info(usertext);

        String aiContent=response.chatResponse().getResult().getOutput().getText();
        List<Document> contents=response.chatResponse().getMetadata().get(QuestionAnswerAdvisor.RETRIEVED_DOCUMENTS);
        EvaluationRequest evalRequest = new EvaluationRequest(usertext, contents, aiContent);
        boolean isRelevant=relevancyEvaluator.evaluate(evalRequest).isPass();

        if (!isRelevant) {
            String feedback = !isRelevant ? "Your answer was off-topic." : "You hallucinated facts not in the PDF.";
//
//            ChatClientRequest retryRequest = ChatClientRequest.builder().prompt(new Prompt("\n\n ERROR: " + feedback + " Please try again using ONLY the provided context."))
//                    .build();
//
//            // Recursively call the chain to try again
//            return callAdvisorChain.copy(this).nextCall(retryRequest);

            logger.info(feedback);
        }
        logger.info(""+isRelevant);
        return response;

    }

    @Override
    public String getName() {
        return this.getClass().getName();
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
