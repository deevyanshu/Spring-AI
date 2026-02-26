package com.deevyanshu.advisor.Advisos;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.api.CallAdvisor;
import org.springframework.ai.chat.client.advisor.api.CallAdvisorChain;
import org.springframework.ai.chat.client.advisor.api.StreamAdvisor;
import org.springframework.ai.chat.client.advisor.api.StreamAdvisorChain;
import reactor.core.publisher.Flux;

public class TokenPrintAdvisor implements CallAdvisor, StreamAdvisor {

    private Logger logger= LoggerFactory.getLogger(TokenPrintAdvisor.class);

    @Override
    public ChatClientResponse adviseCall(ChatClientRequest chatClientRequest, CallAdvisorChain callAdvisorChain) {
        logger.info("My token print advisor is called");
        logger.info("Request:-"+chatClientRequest.prompt().getContents());
        // in callAdvisorChain, the nextCall send the chatClientRequest to next advisor and if the advisor
        // is not available then it send it to the llm
        ChatClientResponse chatClientResponse=callAdvisorChain.nextCall(chatClientRequest);
        logger.info("Response received from the model:");
        logger.info("Response:-"+chatClientResponse.chatResponse().getResult().getOutput().getText());
        logger.info("Total tokens:-"+chatClientResponse.chatResponse().getMetadata().getUsage().getTotalTokens());
        return chatClientResponse;
    }

    @Override
    public Flux<ChatClientResponse> adviseStream(ChatClientRequest chatClientRequest, StreamAdvisorChain streamAdvisorChain) {
        Flux<ChatClientResponse> chatClientResponseFlux=streamAdvisorChain.nextStream(chatClientRequest);
        return chatClientResponseFlux;
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
