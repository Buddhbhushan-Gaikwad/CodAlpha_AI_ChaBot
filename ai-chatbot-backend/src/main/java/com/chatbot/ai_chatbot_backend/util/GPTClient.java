package com.chatbot.ai_chatbot_backend.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.*;

@Component
public class GPTClient {

    @Value("${openai.api.key}")
    private String openAiKey;

    @Value("${openai.model:gpt-3.5-turbo}")
    private String model;

    @Value("${openai.api.url:https://api.openai.com/v1/chat/completions}")
    private String apiUrl;

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public GPTClient() {
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();
        this.objectMapper = new ObjectMapper();
    }

    @PostConstruct
    private void init() {
        if (openAiKey == null || openAiKey.isBlank()) {
            throw new IllegalStateException(
                    "OpenAI API key is not set. Set OPENAI_API_KEY environment variable or openai.api.key property.");
        }
    }

    public String answer(String userPrompt) {
        if (userPrompt == null || userPrompt.isBlank()) {
            return "Please provide a valid prompt.";
        }

        try {
            Map<String, Object> payload = new HashMap<>();
            payload.put("model", model);

            Map<String, String> message = new HashMap<>();
            message.put("role", "user");
            message.put("content", userPrompt);

            // Use a List so Jackson serializes to JSON array properly
            List<Map<String, String>> messages = new ArrayList<>();
            messages.add(message);
            payload.put("messages", messages);

            payload.put("max_tokens", 400);
            payload.put("temperature", 0.6);

            String requestBody = objectMapper.writeValueAsString(payload);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(apiUrl))
                    .timeout(Duration.ofSeconds(40))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + openAiKey)
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() / 100 != 2) {
                return "Sorry, the AI service returned an error: " + response.statusCode() + " - " + response.body();
            }

            JsonNode root = objectMapper.readTree(response.body());
            JsonNode choices = root.path("choices");
            if (choices.isArray() && choices.size() > 0) {
                JsonNode content = choices.get(0).path("message").path("content");
                if (content.isTextual()) {
                    return content.asText().trim();
                }
            }

            return "I'm not sure yet — got an empty response from the AI.";
        } catch (Exception e) {
            e.printStackTrace();
            return "Sorry, I couldn't reach the AI service right now.";
        }
    }
}
