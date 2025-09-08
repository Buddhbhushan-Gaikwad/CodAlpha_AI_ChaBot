package com.chatbot.ai_chatbot_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
public class AiChatbotBackendApplication {
    public static void main(String[] args) {
        SpringApplication.run(AiChatbotBackendApplication.class, args);
    }
}