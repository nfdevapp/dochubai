package org.example.backend.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.backend.ai.*;
import org.example.backend.exeptions.DocHubAiException;
import org.example.backend.model.entities.ChatAi;
import org.example.backend.model.entities.Contract;
import org.example.backend.model.entities.Invoice;
import org.example.backend.repository.ChatAiRepo;
import org.example.backend.repository.ContractRepo;
import org.example.backend.repository.InvoiceRepo;
import org.example.backend.utils.enums.PromptType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
public class ChatGPTService {

    private final ChatAiRepo chatAiRepo;
    private final RestClient restClient;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final InvoiceRepo invoiceRepo;
    private final ContractRepo contractRepo;

    public ChatGPTService(ChatAiRepo chatAiRepo, RestClient.Builder builder, @Value("${openai.api-key}") String apiKey, InvoiceRepo invoiceRepo, ContractRepo contractRepo) {
        this.chatAiRepo = chatAiRepo;
        this.invoiceRepo = invoiceRepo;
        this.contractRepo = contractRepo;
        this.restClient = builder
                .baseUrl("https://api.openai.com/v1/chat/completions")
                .defaultHeader("Authorization", "Bearer " + apiKey)
                .build();
    }

    public AiContractAnalysisResult analyzeContractText(String description) {
        try {
            //Prompt aus DB holen
            ChatAi promptEntity = chatAiRepo.findByKey(PromptType.CONTRACT)
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
            throw new DocHubAiException("Fehler bei der AI-Analyse", e);
        }
    }

    public AiInvoiceAnalysisResult analyzeInvoiceText(List<Invoice> invoices) {
        try {
            // Prompt aus DB holen
            ChatAi promptEntity = chatAiRepo.findByKey(PromptType.INVOICE)
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
            throw new DocHubAiException("Fehler bei der AI-Analyse", e);
        }
    }

    //Einfache Suche
    public String askWithContext(String question) {
        try {
            List<Invoice> invoices = invoiceRepo.findAll();
            List<Contract> contracts = contractRepo.findAll();

            StringBuilder contextBuilder = new StringBuilder();

            //CONTRACTS
            contextBuilder.append("VERTRÄGE:\n");
            for (Contract c : contracts) {

                String aiLevelText = switch (c.aiLevel()) {
                    case 1 -> "ist einwandfrei";
                    case 2 -> "sollte überprüft werden";
                    case 3 -> "weist kritische Abweichungen auf";
                    default -> "unbekannt";
                };

                contextBuilder.append(String.format(
                        """
                        Titel: %s
                        Beschreibung: %s
                        Startdatum: %s
                        Enddatum: %s
                        Extrahierter Text: %s
                        AI-Bewertung: %s
                        AI-Analyse: %s
                        Dateiname: %s
                        ---
                        """,
                        c.title(),
                        c.description(),
                        c.startDate(),
                        c.endDate(),
                        c.extractedText(),
                        aiLevelText,
                        c.aiAnalysisText(),
                        c.fileName()
                ));
            }

            //INVOICES
            contextBuilder.append("\nRECHNUNGEN / BELEGE:\n");
            for (Invoice i : invoices) {

                String typeText = i.isInvoice() ? "Zahlungsbeleg" : "Rechnung";

                contextBuilder.append(String.format(
                        """
                        Typ: %s
                        Belegnummer: %s
                        Datum: %s
                        Betrag: %.2f
                        Zweck: %s
                        Dateiname: %s
                        ---
                        """,
                        typeText,
                        i.docNumber(),
                        i.date(),
                        i.amount(),
                        i.purpose(),
                        i.fileName()
                ));
            }

            // Prompt aus DB holen
            ChatAi promptEntity = chatAiRepo.findByKey(PromptType.CHAT)
                    .orElseThrow(() -> new DocHubAiException("Chat-Prompt nicht gefunden in DB"));

            String userPrompt = """
                KONTEXT:
                %s

                FRAGE:
                %s
                """.formatted(contextBuilder.toString(), question);

            OpenAiResponse response = restClient.post()
                    .body(new OpenAiRequest(
                            "gpt-5",
                            List.of(
                                    new OpenAiMessage("system", promptEntity.prompt()),
                                    new OpenAiMessage("user", userPrompt)
                            )
                    ))
                    .retrieve()
                    .body(OpenAiResponse.class);

            assert response != null;
            return response.text();

        } catch (Exception e) {
            throw new DocHubAiException("Fehler bei der Dokumentensuche", e);
        }
    }


    //Rag Suche: Funz noch nicht
    public String askWithContextForRag(String context, String question) {
        try {

            String systemPrompt = """
            Du bist ein Assistenzsystem für Dokumentenfragen.
            Beantworte die Frage ausschließlich anhand des bereitgestellten Kontexts.
            Wenn die Antwort nicht im Kontext enthalten ist, sage:
            "Die Information ist im Dokument nicht enthalten."
            Antworte sachlich und präzise.
            """;

            String userPrompt = """
            KONTEXT:
            %s

            FRAGE:
            %s
            """.formatted(context, question);

            OpenAiResponse response = restClient.post()
                    .body(new OpenAiRequest(
                            "gpt-5",
                            List.of(
                                    new OpenAiMessage("system", systemPrompt),
                                    new OpenAiMessage("user", userPrompt)
                            )
                    ))
                    .retrieve()
                    .body(OpenAiResponse.class);

            assert response != null;
            return response.text();

        } catch (Exception e) {
            throw new DocHubAiException("Fehler bei der RAG-Abfrage", e);
        }
    }


    public void deleteAllPrompts() {
        chatAiRepo.deleteAll();
    }

    public void savePrompt(ChatAi prompt) {
        chatAiRepo.save(prompt);
    }
}