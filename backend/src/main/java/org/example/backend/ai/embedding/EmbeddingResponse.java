package org.example.backend.ai.embedding;

import java.util.List;

public record EmbeddingResponse(
        List<EmbeddingData> data
) {
}