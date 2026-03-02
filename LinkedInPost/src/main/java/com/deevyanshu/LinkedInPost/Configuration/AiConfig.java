package com.deevyanshu.LinkedInPost.Configuration;

import com.deevyanshu.LinkedInPost.Service.SocialMediaBot;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Configuration
public class AiConfig {

    @Bean
    public ChatClient chatClient(ChatClient.Builder builder)
    {
        return builder.build();
    }

    @Bean
    public RestTemplate restTemplate() {
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setBufferRequestBody(true);

        RestTemplate restTemplate = new RestTemplate(factory);

        // Add an interceptor to ensure "Transfer-Encoding" is never sent
        restTemplate.getInterceptors().add((request, body, execution) -> {
            request.getHeaders().remove(HttpHeaders.TRANSFER_ENCODING);
            return execution.execute(request, body);
        });

        return restTemplate;
    }

    @Bean
    public TelegramBotsApi telegramBotsApi(SocialMediaBot socialMediaBot) throws TelegramApiException {
        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
        botsApi.registerBot(socialMediaBot);
        return botsApi;
    }
}
