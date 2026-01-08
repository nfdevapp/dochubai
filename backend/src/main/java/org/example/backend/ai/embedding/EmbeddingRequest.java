package org.example.backend.ai.embedding;

public record EmbeddingRequest(
        String model,
        String input
) {
}
