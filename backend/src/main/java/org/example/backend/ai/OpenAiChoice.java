package org.example.backend.ai;

/**
 * {
 *   "message": {
 *      "role": "assistant",
 *      "content": "Hello! How can I assist you today?",
 *   }
 * }
 */
public record OpenAiChoice(
        OpenAiMessage message
) {
}
