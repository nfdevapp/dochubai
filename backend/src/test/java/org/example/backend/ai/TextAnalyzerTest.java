package org.example.backend.ai;

import org.example.backend.model.entities.Invoice;
import org.example.backend.service.ChatGPTService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TextAnalyzerTest {

    @Mock
    private ChatGPTService chatGPTService;

    private TextAnalyzer textAnalyzer;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        textAnalyzer = new TextAnalyzer(chatGPTService);
    }

    @Test
    void analyzeContractText_delegatesToChatGPTService() {
        String extractedText = "Some contract text";

        AiContractAnalysisResult expectedResult = new AiContractAnalysisResult(2, "Text OK");
        when(chatGPTService.analyzeContractText(extractedText)).thenReturn(expectedResult);

        AiContractAnalysisResult result = textAnalyzer.analyzeContractText(extractedText);

        assertNotNull(result);
        assertEquals(expectedResult.aiLevel(), result.aiLevel());
        assertEquals(expectedResult.aiAnalysisText(), result.aiAnalysisText());

        verify(chatGPTService, times(1)).analyzeContractText(extractedText);
    }

    @Test
    void analyzeInvoiceText_delegatesToChatGPTService() {
        Invoice invoice1 = Invoice.builder()
                .docNumber("INV-001")
                .amount(100.0)
                .build();
        Invoice invoice2 = Invoice.builder()
                .docNumber("INV-002")
                .amount(200.0)
                .build();

        List<Invoice> invoices = List.of(invoice1, invoice2);

        AiInvoiceAnalysisResult expectedResult = new AiInvoiceAnalysisResult("All OK");
        when(chatGPTService.analyzeInvoiceText(invoices)).thenReturn(expectedResult);

        AiInvoiceAnalysisResult result = textAnalyzer.analyzeInvoiceText(invoices);

        assertNotNull(result);
        assertEquals(expectedResult.aiAnalysisText(), result.aiAnalysisText());

        verify(chatGPTService, times(1)).analyzeInvoiceText(invoices);
    }

    @Test
    void analyzeContractText_throwsIfChatGPTServiceThrows() {
        String text = "Failing text";
        when(chatGPTService.analyzeContractText(text)).thenThrow(new RuntimeException("Error"));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> textAnalyzer.analyzeContractText(text));
        assertEquals("Error", ex.getMessage());
    }

    @Test
    void analyzeInvoiceText_throwsIfChatGPTServiceThrows() {
        List<Invoice> invoices = List.of();
        when(chatGPTService.analyzeInvoiceText(invoices)).thenThrow(new RuntimeException("Error"));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> textAnalyzer.analyzeInvoiceText(invoices));
        assertEquals("Error", ex.getMessage());
    }
}
