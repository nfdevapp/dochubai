package org.example.backend.utils.mapper;

import org.example.backend.exeptions.DocHubAiException;
import org.example.backend.model.dto.InvoiceDto;
import org.example.backend.model.entities.Invoice;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;

class InvoiceMapperTest {

    private InvoiceMapper invoiceMapper;

    @BeforeEach
    void setUp() {
        invoiceMapper = new InvoiceMapper();
    }

    // ---------- toDto ----------
    @Test
    void toDto_withFile_success() {
        byte[] file = "test".getBytes();
        Invoice invoice = Invoice.builder()
                .id("1")
                .docNumber("DOC-001")
                .date(LocalDate.of(2026, 1, 13))
                .amount(100.0)
                .purpose("Test")
                .isInvoice(true)
                .fileName("file.pdf")
                .file(file)
                .build();

        InvoiceDto dto = invoiceMapper.toDto(invoice);

        assertEquals("1", dto.id());
        assertEquals("DOC-001", dto.docNumber());
        assertEquals("13.01.2026", dto.date());
        assertEquals(100.0, dto.amount());
        assertEquals("Test", dto.purpose());
        assertTrue(dto.isInvoice());
        assertEquals("file.pdf", dto.fileName());
        assertEquals(Base64.getEncoder().encodeToString(file), dto.fileBase64());
    }

    @Test
    void toDto_withoutFile_success() {
        Invoice invoice = Invoice.builder()
                .id("1")
                .docNumber("DOC-001")
                .date(LocalDate.of(2026, 1, 13))
                .amount(100.0)
                .purpose("Test")
                .isInvoice(true)
                .fileName("file.pdf")
                .build();

        InvoiceDto dto = invoiceMapper.toDto(invoice);

        assertNull(dto.fileBase64());
    }

    // ---------- fromDto ----------
    @Test
    void fromDto_withValidBase64AndDate_success() {
        String base64 = Base64.getEncoder().encodeToString("data".getBytes());
        InvoiceDto dto = InvoiceDto.builder()
                .docNumber("DOC-001")
                .date("13.01.2026")
                .amount(50.0)
                .purpose("Desc")
                .isInvoice(true)
                .fileName("file.pdf")
                .fileBase64(base64)
                .build();

        Invoice invoice = invoiceMapper.fromDto(dto);

        assertEquals("DOC-001", invoice.docNumber());
        assertEquals(LocalDate.of(2026, 1, 13), invoice.date());
        assertEquals(50.0, invoice.amount());
        assertEquals("Desc", invoice.purpose());
        assertTrue(invoice.isInvoice());
        assertEquals("file.pdf", invoice.fileName());
        assertArrayEquals("data".getBytes(), invoice.file());
    }

    @Test
    void fromDto_withInvalidBase64_throwsException() {
        InvoiceDto dto = InvoiceDto.builder()
                .docNumber("DOC-001")
                .date("13.01.2026")
                .fileBase64("not-base64")
                .build();

        DocHubAiException ex = assertThrows(DocHubAiException.class,
                () -> invoiceMapper.fromDto(dto));
        assertTrue(ex.getMessage().contains("Error decoding file from Base64"));
    }

    @Test
    void fromDto_withInvalidDate_throwsException() {
        InvoiceDto dto = InvoiceDto.builder()
                .docNumber("DOC-001")
                .date("31-12-2026")
                .build();

        DocHubAiException ex = assertThrows(DocHubAiException.class,
                () -> invoiceMapper.fromDto(dto));
        assertTrue(ex.getMessage().contains("Error parsing date"));
    }

    // ---------- toDtoWithoutFile ----------
    @Test
    void toDtoWithoutFile_success() {
        byte[] file = "test".getBytes();
        Invoice invoice = Invoice.builder()
                .id("1")
                .docNumber("DOC-001")
                .date(LocalDate.of(2026, 1, 13))
                .amount(100.0)
                .purpose("Test")
                .isInvoice(true)
                .fileName("file.pdf")
                .file(file)
                .build();

        InvoiceDto dto = invoiceMapper.toDtoWithoutFile(invoice);

        assertEquals("1", dto.id());
        assertEquals("DOC-001", dto.docNumber());
        assertEquals("13.01.2026", dto.date());
        assertEquals(100.0, dto.amount());
        assertEquals("Test", dto.purpose());
        assertTrue(dto.isInvoice());
        assertEquals("file.pdf", dto.fileName());
        assertNull(dto.fileBase64());
    }

    // ---------- toDtoForChart ----------
    @Test
    void toDtoForChart_success() {
        Invoice invoice = Invoice.builder()
                .id("1")
                .date(LocalDate.of(2026, 1, 13))
                .amount(123.45)
                .build();

        InvoiceDto dto = invoiceMapper.toDtoForChart(invoice);

        assertEquals("1", dto.id());
        assertEquals("13.01.2026", dto.date());
        assertEquals(123.45, dto.amount());
        assertNull(dto.docNumber());
        assertNull(dto.purpose());
        assertFalse(dto.isInvoice());
        assertNull(dto.fileName());
        assertNull(dto.fileBase64());
    }
}
