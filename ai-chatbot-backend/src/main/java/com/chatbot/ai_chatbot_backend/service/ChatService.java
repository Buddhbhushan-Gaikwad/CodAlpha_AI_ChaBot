package com.chatbot.ai_chatbot_backend.service;

import com.chatbot.ai_chatbot_backend.util.GPTClient;
import org.springframework.stereotype.Service;

@Service
public class ChatService {

    private final GPTClient gpt;

    public ChatService(GPTClient gpt) {
        this.gpt = gpt;
    }

    public String reply(String rawQuestion) {
        if (rawQuestion == null || rawQuestion.isBlank()) {
            return "Please ask a valid question.";
        }

        // fallback → AI answer
        return gpt.answer(rawQuestion);
    }
}
