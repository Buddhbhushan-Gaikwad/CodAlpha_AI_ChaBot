package com.chatbot.ai_chatbot_backend.service;

import org.springframework.stereotype.Service;
import java.util.*;
import java.util.regex.*;

@Service
public class NlpService {

    public static class AnalysisResult {
        private String intent;
        private Map<String, String> entities = new HashMap<>();
        private List<String> tokens = new ArrayList<>();

        public String getIntent() { return intent; }
        public void setIntent(String intent) { this.intent = intent; }
        public Map<String, String> getEntities() { return entities; }
        public void setEntities(Map<String, String> entities) { this.entities = entities; }
        public List<String> getTokens() { return tokens; }
        public void setTokens(List<String> tokens) { this.tokens = tokens; }
    }

    private final Map<String, List<String>> intentKeywords = Map.of(
            "greeting", Arrays.asList("hi", "hello", "hey", "namaste"),
            "goodbye", Arrays.asList("bye", "goodbye", "see you", "take care"),
            "ask_time", Arrays.asList("time", "what time", "current time"),
            "ask_weather", Arrays.asList("weather", "rain", "sunny", "forecast"),
            "smalltalk_thanks", Arrays.asList("thanks", "thank you", "thx"),
            "about_you", Arrays.asList("about you","what is you","yourself")
    );

    private final Pattern locationPattern = Pattern.compile("in ([A-Za-z _-]+)", Pattern.CASE_INSENSITIVE);

    public AnalysisResult analyze(String text) {
        AnalysisResult result = new AnalysisResult();
        if (text == null || text.isBlank()) {
            result.setIntent("unknown");
            return result;
        }

        String lower = text.toLowerCase(Locale.ROOT);

        // simple tokenization
        List<String> tokens = Arrays.asList(lower.split("\\s+"));
        result.setTokens(tokens);

        // intent detection
        String detected = "unknown";
        for (var e : intentKeywords.entrySet()) {
            for (String kw : e.getValue()) {
                if (lower.contains(kw)) {
                    detected = e.getKey();
                    break;
                }
            }
            if (!detected.equals("unknown")) break;
        }
        result.setIntent(detected);

        // entity extraction (location)
        Matcher m = locationPattern.matcher(text);
        if (m.find()) {
            result.getEntities().put("location", m.group(1).trim());
        }

        return result;
    }
}

