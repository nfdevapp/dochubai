package org.example.backend.service;

import lombok.RequiredArgsConstructor;
import org.example.backend.ai.AiInvoiceAnalysisResult;
import org.example.backend.ai.TextAnalyzer;
import org.example.backend.exeptions.DocHubAiException;
import org.example.backend.model.dto.InvoiceAiDto;
import org.example.backend.model.dto.InvoiceDto;
import org.example.backend.model.entities.Invoice;
import org.example.backend.model.entities.InvoiceAi;
import org.example.backend.repository.InvoiceAiRepo;
import org.example.backend.repository.InvoiceRepo;
import org.example.backend.utils.mapper.InvoiceMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InvoiceService {

    private final InvoiceRepo invoiceRepo;
    private final InvoiceAiRepo  invoiceAiRepo;
    private final InvoiceMapper invoiceMapper;
    private final TextAnalyzer textAnalyzer;

    public InvoiceDto getInvoiceById(String id) {
        Invoice invoice = invoiceRepo.findById(id)
                .orElseThrow(() -> new DocHubAiException("Invoice not found: " + id));
        return invoiceMapper.toDto(invoice);
    }

    public List<InvoiceDto> getAllInvoices() {
        List<Invoice> invoices = invoiceRepo.findAll();
        List<InvoiceDto> invoiceDtos = invoices.stream()
                .map(invoiceMapper::toDtoWithoutFile)
                .collect(Collectors.toList());
        return invoiceDtos;
    }

    public InvoiceDto updateInvoice(String id, InvoiceDto invoiceDto) {
        Invoice oldData = invoiceRepo.findById(id)
                .orElseThrow(() -> new DocHubAiException("Invoice not found: " + id));

        // Mapping
        Invoice mapped = invoiceMapper.fromDto(invoiceDto);

        Invoice updated = oldData
                .withDocNumber(mapped.docNumber())
                .withDate(mapped.date())
                .withAmount(mapped.amount())
                .withPurpose(mapped.purpose())
                .withInvoice(mapped.isInvoice())
                .withFileName(mapped.fileName())
                .withFile(mapped.file());

        invoiceRepo.save(updated);
        return invoiceMapper.toDtoWithoutFile(updated);
    }

    public void deleteInvoice(String id) {
        Invoice invoice = invoiceRepo.findById(id)
                .orElseThrow(() -> new DocHubAiException("Invoice not found: " + id));
        invoiceRepo.delete(invoice);
    }

    public InvoiceDto createInvoice(InvoiceDto invoiceDto) {
        // Mapping
        Invoice mapped = invoiceMapper.fromDto(invoiceDto);
        Invoice created = invoiceRepo.save(mapped);
        return invoiceMapper.toDtoWithoutFile(created);
    }

    public List<InvoiceDto> getInvoiceChart() {
        // Zahlungsbelege nur von den letzten drei Monaten
        LocalDate threeMonthsAgo = LocalDate.now().minusMonths(3);
        List<Invoice> invoices = invoiceRepo.findInvoicesFrom(threeMonthsAgo);
        List<InvoiceDto> invoiceDtos = invoices.stream()
                .map(invoiceMapper::toDtoForChart)
                .collect(Collectors.toList());
        return invoiceDtos;
    }

    public InvoiceAiDto getLastInvoiceAiAnalysis() {
        return invoiceAiRepo.findAll().stream()
                .findFirst()
                .map(ai -> InvoiceAiDto.builder()
                        .aiAnalysisText(ai.aiAnalysisText())
                        .build())
                .orElse(InvoiceAiDto.builder()
                        .aiAnalysisText(null)
                        .build());
    }


    public InvoiceAiDto runInvoiceAiAnalysis() {
        // Zahlungsbelege nur von den letzten drei Monaten
        LocalDate threeMonthsAgo = LocalDate.now().minusMonths(3);
        List<Invoice> invoices = invoiceRepo.findInvoicesFrom(threeMonthsAgo);

        AiInvoiceAnalysisResult result;
        try {
            result = textAnalyzer.analyzeInvoiceText(invoices);
        } catch (Exception e) {
            throw new DocHubAiException("Error during AI analysis: " + e.getMessage());
        }

        invoiceAiRepo.deleteAll();

        InvoiceAi saved = invoiceAiRepo.save(
                InvoiceAi.builder()
                        .aiAnalysisText(result.aiAnalysisText())
                        .build()
        );

        return InvoiceAiDto.builder()
                .aiAnalysisText(saved.aiAnalysisText())
                .build();
    }

    public void deleteAiInvoices() {
        invoiceAiRepo.deleteAll();
    }

    public void createTestDataInvoice(Invoice invoice) {
        invoiceRepo.save(invoice);
    }


    public void deleteAllInvoices() {
        invoiceRepo.deleteAll();
    }
}
