package org.example.backend.service;

import org.example.backend.ai.*;
import org.example.backend.exeptions.DocHubAiException;
import org.example.backend.model.dto.ChatAiDto;
import org.example.backend.model.entities.Contract;
import org.example.backend.model.entities.Invoice;
import org.example.backend.model.entities.PromptsAi;
import org.example.backend.repository.ChatAiRepo;
import org.example.backend.repository.ContractRepo;
import org.example.backend.repository.InvoiceRepo;
import org.example.backend.utils.enums.PromptType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestClient;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChatGPTServiceTest {

    @Mock
    private ChatAiRepo chatAiRepo;

    @Mock
    private InvoiceRepo invoiceRepo;

    @Mock
    private ContractRepo contractRepo;

    @Mock
    private RestClient restClient;

    @Mock
    private RestClient.Builder restClientBuilder;

    @Mock
    private RestClient.RequestBodyUriSpec requestSpec;

    @Mock
    private RestClient.RequestHeadersSpec<?> headersSpec;

    @Mock
    private RestClient.ResponseSpec responseSpec;

    @InjectMocks
    private ChatGPTService chatGPTService;

    @BeforeEach
    void setup() {
        when(restClientBuilder
                .baseUrl(anyString()))
                .thenReturn(restClientBuilder);

        when(restClientBuilder
                .defaultHeader(anyString(), anyString()))
                .thenReturn(restClientBuilder);

        when(restClientBuilder.build())
                .thenReturn(restClient);

        when(restClient.post())
                .thenReturn(requestSpec);

        when(requestSpec.body(any()))
                .thenAnswer(invocation -> headersSpec);

        when(headersSpec.retrieve())
                .thenReturn(responseSpec);
    }

    // ---------- analyzeContractText ----------

    @Test
    void analyzeContractText_success() throws Exception {
        PromptsAi prompt = new PromptsAi(null, PromptType.CONTRACT, "PROMPT:");
        when(chatAiRepo.findByKey(PromptType.CONTRACT))
                .thenReturn(Optional.of(prompt));

        OpenAiResponse response = mock(OpenAiResponse.class);
        when(response.text())
                .thenReturn("{\"aiLevel\":1,\"aiAnalysisText\":\"OK\"}");

        when(responseSpec.body(OpenAiResponse.class))
                .thenReturn(response);

        AiContractAnalysisResult result =
                chatGPTService.analyzeContractText("Text");

        assertEquals(1, result.aiLevel());
        assertEquals("OK", result.aiAnalysisText());
    }

    @Test
    void analyzeContractText_promptMissing() {
        when(chatAiRepo.findByKey(PromptType.CONTRACT))
                .thenReturn(Optional.empty());

        assertThrows(
                DocHubAiException.class,
                () -> chatGPTService.analyzeContractText("Text")
        );
    }

    // ---------- analyzeInvoiceText ----------

    @Test
    void analyzeInvoiceText_success() throws Exception {
        PromptsAi prompt = new PromptsAi(null, PromptType.INVOICE, "PROMPT:");
        when(chatAiRepo.findByKey(PromptType.INVOICE))
                .thenReturn(Optional.of(prompt));

        Invoice invoice = new Invoice(
                "1",
                "DOC-001",
                LocalDate.now(),
                100.0,
                "Test",
                true,
                "file.pdf",
                null
        );

        OpenAiResponse response = mock(OpenAiResponse.class);
        when(response.text())
                .thenReturn("{\"aiAnalysisText\":\"Analyse\"}");

        when(responseSpec.body(OpenAiResponse.class))
                .thenReturn(response);

        AiInvoiceAnalysisResult result =
                chatGPTService.analyzeInvoiceText(List.of(invoice));

        assertEquals("Analyse", result.aiAnalysisText());
    }

    // ---------- askWithContextWithHistory ----------

    @Test
    void askWithContextWithHistory_success() {
        when(invoiceRepo.findAll()).thenReturn(List.of());
        when(contractRepo.findAll()).thenReturn(List.of());

        PromptsAi prompt = new PromptsAi(null, PromptType.CHAT, "SYSTEM");
        when(chatAiRepo.findByKey(PromptType.CHAT))
                .thenReturn(Optional.of(prompt));

        OpenAiResponse response = mock(OpenAiResponse.class);
        when(response.text()).thenReturn("Antwort");

        when(responseSpec.body(OpenAiResponse.class))
                .thenReturn(response);

        String result = chatGPTService.askWithContextWithHistory(
                List.of(new ChatAiDto("Frage", "Antwort")),
                "Neue Frage"
        );

        assertEquals("Antwort", result);
    }

    // ---------- deleteAllPrompts ----------

    @Test
    void deleteAllPrompts_success() {
        chatGPTService.deleteAllPrompts();
        verify(chatAiRepo).deleteAll();
    }

    // ---------- savePrompt ----------

    @Test
    void savePrompt_success() {
        PromptsAi prompt = new PromptsAi(null, PromptType.CHAT, "TEXT");

        chatGPTService.savePrompt(prompt);

        verify(chatAiRepo).save(prompt);
    }
}
