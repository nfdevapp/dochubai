package org.example.backend.ai;

import org.springframework.stereotype.Component;

@Component
public class TextAnalyzer {

    private final ChatGPTService chatGPTService;

    public TextAnalyzer(ChatGPTService chatGPTService) {
        this.chatGPTService = chatGPTService;
    }

    public AiAnalysisResult analyzeText(String extractedText) {
        return chatGPTService.analyzeText(extractedText);
    }
}