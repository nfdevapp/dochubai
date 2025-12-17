package org.example.backend.ai;

import java.util.List;

/**
 * {
 *     "model": "gpt-5",
 *     "messages": [
 *       {
 *         "role": "developer",
 *         "content": "You are a helpful assistant."
 *       }
 *     ]
 *   }
 */
public record OpenAiRequest(
        String model,
        List<OpenAiMessage> messages
) {
    public OpenAiRequest(String message) {
        this("gpt-5", List.of(new OpenAiMessage("user", message)));
    }
}

