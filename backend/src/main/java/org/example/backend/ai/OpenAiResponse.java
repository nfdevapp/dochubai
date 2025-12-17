package org.example.backend.ai;

import java.util.List;

/**
 * {
 *   "choices": [
 *     {
 *       "index": 0,
 *       "message": {
 *         "role": "assistant",
 *         "content": "Hello! How can I assist you today?",
 *       }
 *     }
 *   ]
 * }
 */
public record OpenAiResponse(
        List<OpenAiChoice> choices
) {
    public String text() {
        return choices.getFirst().message().content();
    }
}