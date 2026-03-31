package com.aaryan.SpringEcom.service;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.Collections;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.beans.factory.annotation.Value;

@Service
public class AiImageGeneratorService {

    // The universal Stable Diffusion 3.5 / Ultra endpoint
    private static final String API_URL = "https://api.stability.ai/v2beta/stable-image/generate/sd3";
    
    @Value("${STABILITY_API_KEY}")
    private String stabilityApiKey;
    
    public byte[] generateImage(String imagePrompt) {
        RestTemplate restTemplate = new RestTemplate();

        // 1. Headers: Must be multipart/form-data
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(stabilityApiKey);
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.setAccept(Collections.singletonList(MediaType.valueOf("image/*")));

        // 2. Body: Using LinkedMultiValueMap for multipart/form-data
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("prompt", imagePrompt);
        body.add("model", "sd3.5-flash"); // Set to SD 3.5 Flash as requested
        body.add("output_format", "png"); // Optional: specify format
        body.add("aspect_ratio", "1:1");  // Optional: common for SD 3.5

        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(body, headers);

        // 3. API Call
        try {
            ResponseEntity<byte[]> response = restTemplate.exchange(
                    API_URL,
                    HttpMethod.POST,
                    request,
                    byte[].class
            );
            return response.getBody();
        } catch (Exception e) {
            System.err.println("Error calling Stability AI: " + e.getMessage());
            throw e;
        }
    }
}