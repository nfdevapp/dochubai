package org.example.backend.ai;

/**
 *  {
 *      "role": "developer",
 *      "content": "You are a helpful assistant."
 *  }
 */
public record OpenAiMessage(
        String role,
        String content
) {
}

