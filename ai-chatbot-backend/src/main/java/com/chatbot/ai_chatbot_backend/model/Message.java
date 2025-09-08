package com.chatbot.ai_chatbot_backend.model;

import jakarta.persistence.Entity;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.time.LocalDateTime;
@Document(collection = "messages")
public class Message implements Serializable {

    @Id
    private String id;
    private String sender;      // Who sent the message (e.g., "User" or "ChatBot")
    private String content;     // Message text
    private String type;        // Message type (e.g., "CHAT", "JOIN", "LEAVE")
    private LocalDateTime timestamp; // When the message was created

    // Default constructor (needed for serialization/deserialization)
    public Message() {}

    // Constructor with parameters
    public Message(String sender, String content, String type) {
        this.sender = sender;
        this.content = content;
        this.type = type;
        this.timestamp = LocalDateTime.now();
    }

    // Getters and Setters
    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    // toString method for debugging
    @Override
    public String toString() {
        return "Message{" +
                "sender='" + sender + '\'' +
                ", content='" + content + '\'' +
                ", type='" + type + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
