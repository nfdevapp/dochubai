package org.example.backend.ai;

import org.example.backend.model.dto.InvoiceDto;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TextAnalyzer {

    private final ChatGPTService chatGPTService;

    public TextAnalyzer(ChatGPTService chatGPTService) {
        this.chatGPTService = chatGPTService;
    }

    public AiContractAnalysisResult analyzeContractText(String extractedText) {
        return chatGPTService.analyzeContractText(extractedText);
    }

    public AiInvoiceAnalysisResult analyzeInvoiceText(List<InvoiceDto> invoices) {
        return chatGPTService.analyzeInvoiceText(invoices);
    }
}