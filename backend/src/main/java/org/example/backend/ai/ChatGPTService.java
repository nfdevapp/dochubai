package org.example.backend.ai;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class ChatGPTService {

    private final RestClient restClient;

    public ChatGPTService(
            RestClient.Builder builder,
            @Value("${openai.api-key}")
            String apiKey
    ) {
        this.restClient = builder
                .baseUrl("https://api.openai.com/v1/chat/completions")
                .defaultHeader("Authorization", "Bearer " + apiKey)
//                .defaultHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    public String analyzeText(String description) {
        String prompt = "Bewerte den folgenden Vertrag nach diesem Ranking: " +
                "1 = einwandfrei, 2 = sollte überprüft werden, 3 = weist kritische Abweichungen auf. " +
                "Gib außerdem eine kurze Analyse mit maximal 500 Zeichen zum Ranking: " + description;

        OpenAiResponse response = restClient.post()
                .body(new OpenAiRequest(prompt))
                .retrieve()
                .body(OpenAiResponse.class);

        return response.text();
    }

}
