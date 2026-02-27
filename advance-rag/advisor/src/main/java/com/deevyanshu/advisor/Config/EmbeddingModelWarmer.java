package com.deevyanshu.advisor.Config;

import org.springframework.ai.google.genai.text.GoogleGenAiTextEmbeddingModel;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class EmbeddingModelWarmer {
    private final GoogleGenAiTextEmbeddingModel embeddingModel;

    public EmbeddingModelWarmer(GoogleGenAiTextEmbeddingModel embeddingModel) {
        this.embeddingModel = embeddingModel;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void warmUp() {
        // Run this in a background thread so it doesn't block Application Startup

        new Thread(()->{
            try {
                System.out.println("Starting Google GenAI Warmup...");
                // This makes a network call to Google
                int dims = this.embeddingModel.dimensions();
                System.out.println("Warmup complete. Dimensions: " + dims);
            } catch (Exception e) {
                System.err.println("Warmup failed (but app will still run): " + e.getMessage());
            }
        }).start();
    }
}
