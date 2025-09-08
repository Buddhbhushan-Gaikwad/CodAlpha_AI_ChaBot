package com.chatbot.ai_chatbot_backend.controller;

import com.chatbot.ai_chatbot_backend.model.Message;
import com.chatbot.ai_chatbot_backend.service.ChatService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {

    private final ChatService chat;

    public ChatController(ChatService chat) {
        this.chat = chat;
    }

    // Frontend sends to /app/sendMessage
    @MessageMapping("/sendMessage")
    @SendTo("/topic/public") // broadcast bot reply
    public Message processMessage(Message message) {
        String answer = chat.reply(message.getContent());
        return new Message("ChatBot", answer, "CHAT");
    }
}
