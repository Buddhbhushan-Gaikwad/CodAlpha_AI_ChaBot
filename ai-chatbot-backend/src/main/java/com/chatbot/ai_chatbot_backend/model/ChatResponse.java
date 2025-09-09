package com.chatbot.ai_chatbot_backend.model;

import java.util.HashMap;
import java.util.Map;

public class ChatResponse {
    private String reply;
    private String intent;
    private Map<String, String> entities = new HashMap<>();
    private String timestamp;

    public String getReply() { return reply; }
    public void setReply(String reply) { this.reply = reply; }
    public String getIntent() { return intent; }
    public void setIntent(String intent) { this.intent = intent; }
    public Map<String, String> getEntities() { return entities; }
    public void setEntities(Map<String, String> entities) { this.entities = entities; }
    public String getTimestamp() { return timestamp; }
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }
}

