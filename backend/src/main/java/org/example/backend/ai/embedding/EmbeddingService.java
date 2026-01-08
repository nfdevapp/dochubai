package org.example.backend.ai.embedding;

import org.example.backend.exeptions.DocHubAiException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
public class EmbeddingService {

    private final RestClient restClient;

    public EmbeddingService(
            RestClient.Builder builder,
            @Value("${openai.api-key}") String apiKey
    ) {
        this.restClient = builder
                .baseUrl("https://api.openai.com/v1/embeddings")
                .defaultHeader("Authorization", "Bearer " + apiKey)
                .build();
    }

    /**
     * Erstellt ein Embedding (Zahlenvektor) für einen Text.
     */
    public List<Double> createEmbedding(String text) {
        try {
            EmbeddingRequest request = new EmbeddingRequest(
                    "text-embedding-3-small",
                    text
            );

            EmbeddingResponse response = restClient.post()
                    .body(request)
                    .retrieve()
                    .body(EmbeddingResponse.class);

            if (response == null || response.data().isEmpty()) {
                throw new DocHubAiException("Leere Embedding-Antwort von OpenAI");
            }

            return response.data().getFirst().embedding();

        } catch (Exception e) {
            throw new DocHubAiException("Fehler beim Erstellen des Embeddings");
        }
    }
}