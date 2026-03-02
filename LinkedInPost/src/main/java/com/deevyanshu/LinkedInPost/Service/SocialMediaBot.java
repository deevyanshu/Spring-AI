package com.deevyanshu.LinkedInPost.Service;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.objects.File;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.springframework.http.*;

import java.util.*;
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
    private final Map<Long, String> pendingMedia = new ConcurrentHashMap<>();
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

        if (update.hasMessage()) {
            long chatId = update.getMessage().getChatId();
            String userText = update.getMessage().getCaption() != null ? update.getMessage().getCaption() : update.getMessage().getText();

            // Handle Photo Upload
            if (update.getMessage().hasPhoto()) {
                sendTelegramMessage(chatId, "🖼️ Processing your image for LinkedIn...");
                String mediaUrn = handleImageUpload(update.getMessage().getPhoto(), chatId);
                if (mediaUrn != null) {
                    pendingMedia.put(chatId, mediaUrn);
                    sendTelegramMessage(chatId, "✅ Image attached to draft.");
                }
            }

            // Handle Text / Post Generation
            if (userText != null) {
                if (pendingPosts.containsKey(chatId)) {
                    sendTelegramMessage(chatId, "🔄 Revising post based on your feedback...");
                    String revisedPost = revisePost(pendingPosts.get(chatId), userText, chatId);
                    if (revisedPost != null) askForApproval(chatId, revisedPost);
                } else {
                    sendTelegramMessage(chatId, "🤖 AI is drafting your LinkedIn post...");
                    String aiPost = generatePost(userText, chatId);
                    if (aiPost != null) askForApproval(chatId, aiPost);
                }
            } else if (update.getMessage().hasPhoto() && userText == null && !pendingPosts.containsKey(chatId)) {
                sendTelegramMessage(chatId, "Please send a caption or text topic to generate the post content!");
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

    private String handleImageUpload(List<PhotoSize> photos, long chatId) {
        try {
            // Get highest resolution photo
            PhotoSize photo = photos.stream().max(Comparator.comparing(PhotoSize::getFileSize)).orElse(null);
            if (photo == null) return null;

            // 1. Get file path from Telegram
            GetFile getFile = new GetFile();
            getFile.setFileId(photo.getFileId());
            File file = execute(getFile);
            byte[] imageBytes = restTemplate.getForObject("https://api.telegram.org/file/bot" + botToken + "/" + file.getFilePath(), byte[].class);

            // 2. Register Upload with LinkedIn
            String personUrn = getMyPersonUrn();
            String registerUrl = "https://api.linkedin.com/v2/assets?action=registerUpload";

            Map<String, Object> registerBody = Map.of(
                    "registerUploadRequest", Map.of(
                            "recipes", List.of("urn:li:digitalmediaRecipe:feedshare-image"),
                            "owner", personUrn,
                            "serviceRelationships", List.of(Map.of(
                                    "relationshipType", "OWNER",
                                    "identifier", "urn:li:userGeneratedContent"
                            ))
                    )
            );

            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(linkedinToken);
            headers.setContentType(MediaType.APPLICATION_JSON);

            ResponseEntity<Map> registerResponse = restTemplate.postForEntity(registerUrl, new HttpEntity<>(registerBody, headers), Map.class);

            if (registerResponse.getStatusCode().is2xxSuccessful() && registerResponse.getBody() != null) {
                Map value = (Map) registerResponse.getBody().get("value");
                Map uploadMechanism = (Map) value.get("uploadMechanism");
                Map uploadRequest = (Map) uploadMechanism.get("com.linkedin.digitalmedia.uploading.MediaUploadHttpRequest");

                String uploadUrl = (String) uploadRequest.get("uploadUrl");
                String assetUrn = (String) value.get("asset");

                // 3. Upload binary to LinkedIn
                HttpHeaders uploadHeaders = new HttpHeaders();
                uploadHeaders.setBearerAuth(linkedinToken);
                uploadHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);

                restTemplate.exchange(uploadUrl, HttpMethod.POST, new HttpEntity<>(imageBytes, uploadHeaders), String.class);

                logger.info("Image successfully uploaded to LinkedIn: {}", assetUrn);
                return assetUrn;
            }
        } catch (Exception e) {
            logger.error("Image processing error", e);
            sendTelegramMessage(chatId, "❌ Media upload failed: " + e.getMessage());
        }
        return null;
    }


    private void handleCallback(Update update) {
        String callbackData = update.getCallbackQuery().getData();
        long chatId = update.getCallbackQuery().getMessage().getChatId();

        if ("approve_post".equals(callbackData)) {
            String content = pendingPosts.get(chatId);
            String mediaUrn = pendingMedia.get(chatId);

            if (content != null) {
                sendTelegramMessage(chatId, "🚀 Publishing to LinkedIn...");
                boolean success = postToLinkedIn(content, mediaUrn);
                if (success) {
                    sendTelegramMessage(chatId, "✅ Successfully published!");
                    pendingPosts.remove(chatId);
                    pendingMedia.remove(chatId);
                } else {
                    sendTelegramMessage(chatId, "❌ LinkedIn API Error. Check console logs.");
                }
            }
        } else if ("cancel_post".equals(callbackData)) {
            pendingPosts.remove(chatId);
            pendingMedia.remove(chatId);
            sendTelegramMessage(chatId, "🗑️ Draft discarded.");
        }
    }

    private String getMyPersonUrn() {
        if (cachedPersonUrn != null) return cachedPersonUrn;
        try {
            String url = "https://api.linkedin.com/v2/userinfo";
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(linkedinToken);
            HttpEntity<Void> entity = new HttpEntity<>(headers);
            ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                String sub = (String) response.getBody().get("sub");
                cachedPersonUrn = "urn:li:person:" + sub;
                return cachedPersonUrn;
            }
        } catch (Exception e) {
            logger.error("LinkedIn ID Fetch Error", e);
        }
        return null;
    }

    private boolean postToLinkedIn(String content, String mediaUrn) {
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

            if (mediaUrn != null) {
                shareContent.put("shareMediaCategory", "IMAGE");
                shareContent.put("media", List.of(Map.of(
                        "status", "READY",
                        "media", mediaUrn
                )));
            } else {
                shareContent.put("shareMediaCategory", "NONE");
            }

            body.put("specificContent", Map.of("com.linkedin.ugc.ShareContent", shareContent));
            body.put("visibility", Map.of("com.linkedin.ugc.MemberNetworkVisibility", "PUBLIC"));

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);
            ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);
            return response.getStatusCode().is2xxSuccessful();
        } catch (Exception e) {
            logger.error("LinkedIn API Final Error", e);
            return false;
        }
    }

    private String generatePost(String topic, long chatId) {
        try {
            return chatClient.prompt("Write a short, engaging LinkedIn post and do not include any unecessary characters about: " + topic).call().content();
        } catch (Exception e) {
            logger.error("AI Error", e);
            sendTelegramMessage(chatId, "❌ AI Error: " + e.getMessage());
            return null;
        }
    }

    private String revisePost(String original, String feedback, long chatId) {
        try {
            return chatClient.prompt("Original: " + original + "\n\nUpdate: " + feedback).call().content();
        } catch (Exception e) {
            logger.error("AI Error", e);
            return null;
        }
    }

    private void askForApproval(long chatId, String postContent) {
        pendingPosts.put(chatId, postContent);
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        String suffix = pendingMedia.containsKey(chatId) ? "\n\n(🖼️ Image Attached)" : "";
        message.setText("📝 Draft:" + suffix + "\n\n" + postContent + "\n\nReply to edit or click below:");

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        List<InlineKeyboardButton> row = new ArrayList<>();

        InlineKeyboardButton publishBtn = new InlineKeyboardButton();
        publishBtn.setText("✅ Publish");
        publishBtn.setCallbackData("approve_post");

        InlineKeyboardButton discardBtn = new InlineKeyboardButton();
        discardBtn.setText("🗑️ Discard");
        discardBtn.setCallbackData("cancel_post");

        row.add(publishBtn);
        row.add(discardBtn); // Added Discard button to the row
        rows.add(row);
        markup.setKeyboard(rows);
        message.setReplyMarkup(markup);

        try { execute(message); } catch (Exception e) { logger.error("Telegram error", e); }
    }

    private void sendTelegramMessage(long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(text);
        try { execute(message); } catch (Exception e) { logger.error("Telegram error", e); }
    }

}
