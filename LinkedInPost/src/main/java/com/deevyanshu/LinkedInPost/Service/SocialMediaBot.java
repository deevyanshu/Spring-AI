package com.deevyanshu.LinkedInPost.Service;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.springframework.http.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@Service
public class SocialMediaBot extends TelegramLongPollingBot {

    private static final Logger logger = LoggerFactory.getLogger(SocialMediaBot.class);

    @Value("${telegram.bot.token}")
    private String botToken;

    @Value("${telegram.bot.username}")
    private String botUsername;

    @Value("${linkedin.access.token}")
    private String linkedinToken;

    private final ChatClient chatClient;
    private final RestTemplate restTemplate;

    // State management: ChatId -> Current Pending Post Content
    private final Map<Long, String> pendingPosts = new ConcurrentHashMap<>();
    // Cached Person URN to avoid repeated API calls
    private String cachedPersonUrn = null;

    public SocialMediaBot(ChatClient chatClient, RestTemplate restTemplate) {
        this.chatClient = chatClient;
        this.restTemplate = restTemplate;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasCallbackQuery()) {
            handleCallback(update);
            return;
        }

        if (update.hasMessage() && update.getMessage().hasText()) {
            long chatId = update.getMessage().getChatId();
            String userText = update.getMessage().getText();

            if (pendingPosts.containsKey(chatId)) {
                sendTelegramMessage(chatId, "🔄 Revising post based on your feedback...");
                String revisedPost = revisePost(pendingPosts.get(chatId), userText);
                askForApproval(chatId, revisedPost);
            } else {
                sendTelegramMessage(chatId, "🤖 AI is drafting your LinkedIn post...");
                String aiPost = generatePost(userText,chatId);
                if(aiPost!=null) askForApproval(chatId, aiPost);
            }
        }
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    private void handleCallback(Update update) {
        String callbackData = update.getCallbackQuery().getData();
        long chatId = update.getCallbackQuery().getMessage().getChatId();

        if ("approve_post".equals(callbackData)) {
            String content = pendingPosts.get(chatId);
            if (content != null) {
                sendTelegramMessage(chatId, "🚀 Publishing to your personal LinkedIn profile...");
                boolean success = postToLinkedIn(content);
                if (success) {
                    sendTelegramMessage(chatId, "✅ Successfully published!");
                    pendingPosts.remove(chatId);
                } else {
                    sendTelegramMessage(chatId, "❌ Failed to publish. Ensure your token has 'w_member_social' scope.");
                }
            }
        } else if ("cancel_post".equals(callbackData)) {
            pendingPosts.remove(chatId);
            sendTelegramMessage(chatId, "🗑️ Draft discarded.");
        }
    }

    private String getMyPersonUrn() {
        if (cachedPersonUrn != null) return cachedPersonUrn;

        try {
            // Using the OpenID Connect userinfo endpoint to get the "sub" (ID)
            String url = "https://api.linkedin.com/v2/userinfo";
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(linkedinToken);

            HttpEntity<Void> entity = new HttpEntity<>(headers);
            ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                String sub = (String) response.getBody().get("sub");
                cachedPersonUrn = "urn:li:person:" + sub;
                logger.info("Resolved personal URN: {}", cachedPersonUrn);
                return cachedPersonUrn;
            }
        } catch (Exception e) {
            logger.error("Failed to fetch personal URN from LinkedIn", e);
        }
        return null;
    }

    private boolean postToLinkedIn(String content) {
        String personUrn = getMyPersonUrn();
        if (personUrn == null) return false;

        try {
            String url = "https://api.linkedin.com/v2/ugcPosts";
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(linkedinToken);
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("X-Restli-Protocol-Version", "2.0.0");

            Map<String, Object> body = new HashMap<>();
            body.put("author", personUrn);
            body.put("lifecycleState", "PUBLISHED");
            Map<String, Object> shareContent = new HashMap<>();
            shareContent.put("shareCommentary", Map.of("text", content));
            shareContent.put("shareMediaCategory", "NONE");

            body.put("specificContent", Map.of("com.linkedin.ugc.ShareContent", shareContent));
            body.put("visibility", Map.of("com.linkedin.ugc.MemberNetworkVisibility", "PUBLIC"));

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);
            ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);
            return response.getStatusCode().is2xxSuccessful();
        } catch (Exception e) {
            logger.error("LinkedIn Post Error", e);
            return false;
        }
    }

    private void askForApproval(long chatId, String postContent) {
        pendingPosts.put(chatId, postContent);
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText("📝 *Draft Generated:*\n\n" + postContent + "\n\n_Reply to revise or use buttons to finish._");
       //message.setParseMode("Markdown");

        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline = new ArrayList<>();

        InlineKeyboardButton approveBtn = new InlineKeyboardButton();
        approveBtn.setText("✅ Post to Profile");
        approveBtn.setCallbackData("approve_post");

        InlineKeyboardButton cancelBtn = new InlineKeyboardButton();
        cancelBtn.setText("❌ Discard");
        cancelBtn.setCallbackData("cancel_post");

        rowInline.add(approveBtn);
        rowInline.add(cancelBtn);
        rowsInline.add(rowInline);
        markupInline.setKeyboard(rowsInline);
        message.setReplyMarkup(markupInline);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            logger.error("Telegram error", e);
        }
    }

    private String generatePost(String topic, long chatId) {
        try {
            logger.info("Calling Spring AI for topic: {}", topic);
            String result = chatClient.prompt("Write a professional LinkedIn post but make it humanize and do not use any other character in response about: " + topic).call().content();
            logger.info("AI Generation successful");
            return result;
        } catch (Exception e) {
            logger.error("Error during AI generation", e);
            sendTelegramMessage(chatId, "❌ AI Generation Error: " + e.getMessage());
            return null;
        }
    }

    private String revisePost(String original, String feedback) {
        return chatClient.prompt("Original LinkedIn post: " + original + "\n\nUpdate it based on this feedback: " + feedback).call().content();
    }

    private void sendTelegramMessage(long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(text);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            logger.error("Telegram error", e);
        }
    }

}
