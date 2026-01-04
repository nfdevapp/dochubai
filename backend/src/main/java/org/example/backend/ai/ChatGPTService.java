package org.example.backend.ai;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.backend.exeptions.DocHubAiException;
import org.example.backend.model.entities.ChatGPT;
import org.example.backend.model.entities.Invoice;
import org.example.backend.repository.ChatGPTRepo;
import org.example.backend.utils.enums.PromptType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
public class ChatGPTService {

    private final ChatGPTRepo chatGPTRepo;
    private final RestClient restClient;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public ChatGPTService(ChatGPTRepo chatGPTRepo, RestClient.Builder builder, @Value("${openai.api-key}") String apiKey) {
        this.chatGPTRepo = chatGPTRepo;
        this.restClient = builder
                .baseUrl("https://api.openai.com/v1/chat/completions")
                .defaultHeader("Authorization", "Bearer " + apiKey)
                .build();
    }

    public AiContractAnalysisResult analyzeContractText(String description) {
        try {
            //Prompt aus DB holen
            ChatGPT promptEntity = chatGPTRepo.findByKey(PromptType.CONTRACT)
                    .orElseThrow(() -> new DocHubAiException("Contract-Prompt nicht gefunden in DB"));

            String prompt = promptEntity.prompt() + description;

            OpenAiResponse response = restClient.post()
                    .body(new OpenAiRequest(prompt))
                    .retrieve()
                    .body(OpenAiResponse.class);

            assert response != null;
            String jsonText = response.text();

            return objectMapper.readValue(jsonText, AiContractAnalysisResult.class);

        } catch (Exception e) {
            throw new DocHubAiException("Fehler bei der AI-Analyse");
        }
    }

    public AiInvoiceAnalysisResult analyzeInvoiceText(List<Invoice> invoices) {
        try {
            // Prompt aus DB holen
            ChatGPT promptEntity = chatGPTRepo.findByKey(PromptType.INVOICE)
                    .orElseThrow(() -> new DocHubAiException("Invoice-Prompt nicht gefunden in DB"));

            StringBuilder invoiceText = new StringBuilder();
            for (Invoice inv : invoices) {
                invoiceText.append(String.format(
                        "Datum: %s, Belegnummer: %s, Betrag: %.2f, Zweck: %s%n",
                        inv.date(), inv.docNumber(), inv.amount(), inv.purpose()
                ));
            }

            String prompt = promptEntity.prompt() + invoiceText;

            OpenAiResponse response = restClient.post()
                    .body(new OpenAiRequest(prompt))
                    .retrieve()
                    .body(OpenAiResponse.class);

            assert response != null;
            String jsonText = response.text();

            return objectMapper.readValue(jsonText, AiInvoiceAnalysisResult.class);

        } catch (Exception e) {
            throw new DocHubAiException("Fehler bei der AI-Analyse");
        }
    }

    public void deleteAllPrompts() {
        chatGPTRepo.deleteAll();
    }

    public void savePrompt(ChatGPT prompt) {
        chatGPTRepo.save(prompt);
    }
}