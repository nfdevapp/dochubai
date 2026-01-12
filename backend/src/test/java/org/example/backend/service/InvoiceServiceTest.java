package org.example.backend.service;

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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InvoiceServiceTest {

    @Mock
    private InvoiceRepo invoiceRepo;

    @Mock
    private InvoiceAiRepo invoiceAiRepo;

    @Mock
    private InvoiceMapper invoiceMapper;

    @Mock
    private TextAnalyzer textAnalyzer;

    @InjectMocks
    private InvoiceService invoiceService;

    // ---------- getInvoiceById ----------

    @Test
    void getInvoiceById_success() {
        // GIVEN
        Invoice invoice = mock(Invoice.class);
        InvoiceDto dto = mock(InvoiceDto.class);

        when(invoiceRepo.findById("1")).thenReturn(Optional.of(invoice));
        when(invoiceMapper.toDto(invoice)).thenReturn(dto);

        // WHEN
        InvoiceDto result = invoiceService.getInvoiceById("1");

        // THEN
        assertEquals(dto, result);
        verify(invoiceRepo).findById("1");
        verify(invoiceMapper).toDto(invoice);
    }

    @Test
    void getInvoiceById_notFound() {
        // GIVEN
        when(invoiceRepo.findById("1")).thenReturn(Optional.empty());

        // WHEN / THEN
        assertThrows(
                DocHubAiException.class,
                () -> invoiceService.getInvoiceById("1")
        );
    }

    // ---------- getAllInvoices ----------

    @Test
    void getAllInvoices_success() {
        // GIVEN
        Invoice invoice1 = mock(Invoice.class);
        Invoice invoice2 = mock(Invoice.class);
        InvoiceDto dto1 = mock(InvoiceDto.class);
        InvoiceDto dto2 = mock(InvoiceDto.class);

        when(invoiceRepo.findAll()).thenReturn(List.of(invoice1, invoice2));
        when(invoiceMapper.toDtoWithoutFile(invoice1)).thenReturn(dto1);
        when(invoiceMapper.toDtoWithoutFile(invoice2)).thenReturn(dto2);

        // WHEN
        List<InvoiceDto> result = invoiceService.getAllInvoices();

        // THEN
        assertEquals(2, result.size());
        assertTrue(result.contains(dto1));
        assertTrue(result.contains(dto2));
    }

    // ---------- createInvoice ----------

    @Test
    void createInvoice_success() {
        // GIVEN
        InvoiceDto inputDto = mock(InvoiceDto.class);
        Invoice mapped = mock(Invoice.class);
        Invoice saved = mock(Invoice.class);
        InvoiceDto outputDto = mock(InvoiceDto.class);

        when(invoiceMapper.fromDto(inputDto)).thenReturn(mapped);
        when(invoiceRepo.save(mapped)).thenReturn(saved);
        when(invoiceMapper.toDtoWithoutFile(saved)).thenReturn(outputDto);

        // WHEN
        InvoiceDto result = invoiceService.createInvoice(inputDto);

        // THEN
        assertEquals(outputDto, result);
        verify(invoiceRepo).save(mapped);
    }

    // ---------- updateInvoice ----------

    @Test
    void updateInvoice_success() {
        // GIVEN
        InvoiceDto dto = mock(InvoiceDto.class);

        Invoice oldInvoice = mock(Invoice.class, RETURNS_SELF);
        Invoice mapped = mock(Invoice.class);
        Invoice updated = mock(Invoice.class);

        when(invoiceRepo.findById("1")).thenReturn(Optional.of(oldInvoice));
        when(invoiceMapper.fromDto(dto)).thenReturn(mapped);
        when(oldInvoice.withFile(any())).thenReturn(updated);
        when(invoiceRepo.save(updated)).thenReturn(updated);
        when(invoiceMapper.toDtoWithoutFile(updated)).thenReturn(dto);

        // WHEN
        InvoiceDto result = invoiceService.updateInvoice("1", dto);

        // THEN
        assertEquals(dto, result);
        verify(invoiceRepo).save(updated);
    }

    @Test
    void updateInvoice_notFound() {
        // GIVEN
        when(invoiceRepo.findById("1")).thenReturn(Optional.empty());

        // WHEN / THEN
        assertThrows(
                DocHubAiException.class,
                () -> invoiceService.updateInvoice("1", mock(InvoiceDto.class))
        );
    }

    // ---------- deleteInvoice ----------

    @Test
    void deleteInvoice_success() {
        // GIVEN
        Invoice invoice = mock(Invoice.class);
        when(invoiceRepo.findById("1")).thenReturn(Optional.of(invoice));

        // WHEN
        invoiceService.deleteInvoice("1");

        // THEN
        verify(invoiceRepo).delete(invoice);
    }

    @Test
    void deleteInvoice_notFound() {
        // GIVEN
        when(invoiceRepo.findById("1")).thenReturn(Optional.empty());

        // WHEN / THEN
        assertThrows(
                DocHubAiException.class,
                () -> invoiceService.deleteInvoice("1")
        );
    }

    // ---------- getInvoiceChart ----------

    @Test
    void getInvoiceChart_success() {
        // GIVEN
        Invoice invoice = mock(Invoice.class);
        InvoiceDto dto = mock(InvoiceDto.class);

        when(invoiceRepo.findInvoicesFrom(any(LocalDate.class)))
                .thenReturn(List.of(invoice));
        when(invoiceMapper.toDtoForChart(invoice)).thenReturn(dto);

        // WHEN
        List<InvoiceDto> result = invoiceService.getInvoiceChart();

        // THEN
        assertEquals(1, result.size());
        assertEquals(dto, result.get(0));
    }

    // ---------- getLastInvoiceAiAnalysis ----------

    @Test
    void getLastInvoiceAiAnalysis_whenExists() {
        // GIVEN
        InvoiceAi ai = InvoiceAi.builder()
                .aiAnalysisText("analysis")
                .build();

        when(invoiceAiRepo.findAll()).thenReturn(List.of(ai));

        // WHEN
        InvoiceAiDto result = invoiceService.getLastInvoiceAiAnalysis();

        // THEN
        assertEquals("analysis", result.aiAnalysisText());
    }

    @Test
    void getLastInvoiceAiAnalysis_whenEmpty() {
        // GIVEN
        when(invoiceAiRepo.findAll()).thenReturn(List.of());

        // WHEN
        InvoiceAiDto result = invoiceService.getLastInvoiceAiAnalysis();

        // THEN
        assertNull(result.aiAnalysisText());
    }

    // ---------- runInvoiceAiAnalysis ----------

    @Test
    void runInvoiceAiAnalysis_success() {
        // GIVEN
        Invoice invoice = mock(Invoice.class);
        AiInvoiceAnalysisResult aiResult = new AiInvoiceAnalysisResult("AI RESULT");

        when(invoiceRepo.findInvoicesFrom(any(LocalDate.class)))
                .thenReturn(List.of(invoice));
        when(textAnalyzer.analyzeInvoiceText(anyList()))
                .thenReturn(aiResult);

        when(invoiceAiRepo.save(any(InvoiceAi.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        // WHEN
        InvoiceAiDto result = invoiceService.runInvoiceAiAnalysis();

        // THEN
        assertEquals("AI RESULT", result.aiAnalysisText());
        verify(invoiceAiRepo).deleteAll();
        verify(invoiceAiRepo).save(any(InvoiceAi.class));
    }

    @Test
    void runInvoiceAiAnalysis_whenAnalyzerFails() {
        // GIVEN
        when(invoiceRepo.findInvoicesFrom(any(LocalDate.class)))
                .thenReturn(List.of());
        when(textAnalyzer.analyzeInvoiceText(anyList()))
                .thenThrow(new RuntimeException("AI error"));

        // WHEN / THEN
        assertThrows(
                DocHubAiException.class,
                () -> invoiceService.runInvoiceAiAnalysis()
        );
    }

    // ---------- deleteAiInvoices ----------

    @Test
    void deleteAiInvoices_success() {
        // WHEN
        invoiceService.deleteAiInvoices();

        // THEN
        verify(invoiceAiRepo).deleteAll();
    }

    // ---------- createTestDataInvoice ----------

    @Test
    void createTestDataInvoice_success() {
        // GIVEN
        Invoice invoice = mock(Invoice.class);

        // WHEN
        invoiceService.createTestDataInvoice(invoice);

        // THEN
        verify(invoiceRepo).save(invoice);
    }

    // ---------- deleteAllInvoices ----------

    @Test
    void deleteAllInvoices_success() {
        // WHEN
        invoiceService.deleteAllInvoices();

        // THEN
        verify(invoiceRepo).deleteAll();
    }
}