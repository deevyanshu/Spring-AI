package com.audio.audiotranscribe.Service;

import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.image.ImageResponse;
import org.springframework.ai.openai.OpenAiImageModel;
import org.springframework.ai.openai.OpenAiImageOptions;
import org.springframework.stereotype.Service;

@Service
public class ImageService {

    private final OpenAiImageModel imageModel;

    public ImageService(OpenAiImageModel imageModel) {
        this.imageModel = imageModel;
    }

    public ImageResponse imageResponse(String prompt){

        ImageResponse response=imageModel.call(
            new ImagePrompt(prompt+" "+"with image size less than 300kb", OpenAiImageOptions.builder().model("dall-e-2")
            .N(4).height(1024).width(1024).build())
        );

        return response;

    }
    
}
