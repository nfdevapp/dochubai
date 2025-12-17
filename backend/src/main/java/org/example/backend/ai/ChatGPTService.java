package org.example.backend.ai;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class ChatGPTService {

    private final RestClient restClient;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public ChatGPTService(RestClient.Builder builder, @Value("${openai.api-key}") String apiKey) {
        this.restClient = builder
                .baseUrl("https://api.openai.com/v1/chat/completions")
                .defaultHeader("Authorization", "Bearer " + apiKey)
                .build();
    }

    public AiAnalysisResult analyzeText(String description) {
        try {
            String prompt = "Analysiere den folgenden Vertrag. " +
                    "Bewerte ihn nach diesem Schema:\n" +
                    "1 = ist einwandfrei\n" +
                    "2 = sollte überprüft werden\n" +
                    "3 = weist kritische Abweichungen auf\n\n" +
                    "Antworte AUSSCHLIESSLICH im JSON-Format:\n" +
                    "{\n" +
                    "  \"aiLevel\": 1,\n" +
                    "  \"aiAnalysisText\": \"Text mit maximal 500 Zeichen\"\n" +
                    "}\n\n" +
                    "Vertragstext:\n" + description;

            OpenAiResponse response = restClient.post()
                    .body(new OpenAiRequest(prompt))
                    .retrieve()
                    .body(OpenAiResponse.class);

            String jsonText = response.text();

            return objectMapper.readValue(jsonText, AiAnalysisResult.class);

        } catch (Exception e) {
            throw new RuntimeException("Fehler bei der AI-Analyse", e);
        }
    }
}