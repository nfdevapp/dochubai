package org.example.backend.utils.mapper;

import org.example.backend.exeptions.DocHubAiException;
import org.example.backend.model.dto.InvoiceDto;
import org.example.backend.model.entities.Invoice;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Base64;

@Component
public class InvoiceMapper {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    // Invoice => InvoiceDto
    public InvoiceDto toDto(Invoice invoice) {
        try {
            String fileBase = null;
            if (invoice.file() != null) {
                fileBase = Base64.getEncoder().encodeToString(invoice.file());
            }

            return InvoiceDto.builder()
                    .id(invoice.id())
                    .docNumber(invoice.docNumber())
                    .date(invoice.date() != null ? invoice.date().format(FORMATTER) : null)
                    .amount(invoice.amount())
                    .purpose(invoice.purpose())
                    .isInvoice(invoice.isInvoice())
                    .fileName(invoice.fileName())
                    .fileBase64(fileBase)
                    .build();
        } catch (IllegalArgumentException e) {
            throw new DocHubAiException("Error encoding file to Base64: " + e.getMessage());
        } catch (Exception e) {
            throw new DocHubAiException("Unexpected error while mapping Invoice to DTO: " + e.getMessage());
        }
    }

    // InvoiceDto => Invoice
    public Invoice fromDto(InvoiceDto dto) {
        try {
            byte[] fileBytes = null;

            if (dto.fileBase64() != null) {
                try {
                    fileBytes = Base64.getDecoder().decode(dto.fileBase64());
                } catch (IllegalArgumentException e) {
                    throw new DocHubAiException("Error decoding file from Base64: " + e.getMessage());
                }
            }

            LocalDate date;
            try {
                date = dto.date() != null ? LocalDate.parse(dto.date(), FORMATTER) : null;
            } catch (DateTimeParseException e) {
                throw new DocHubAiException("Error parsing date: " + e.getMessage());
            }

            return Invoice.builder()
                    .docNumber(dto.docNumber())
                    .date(date)
                    .amount(dto.amount())
                    .purpose(dto.purpose())
                    .isInvoice(dto.isInvoice())
                    .fileName(dto.fileName())
                    .file(fileBytes)
                    .build();

        } catch (DocHubAiException e) {
            throw e;
        } catch (Exception e) {
            throw new DocHubAiException("Unexpected error while mapping DTO to Invoice: " + e.getMessage());
        }
    }

    // getAllInvoices: without file
    public InvoiceDto toDtoWithoutFile(Invoice invoice) {
        try {
            return InvoiceDto.builder()
                    .id(invoice.id())
                    .docNumber(invoice.docNumber())
                    .date(invoice.date() != null ? invoice.date().format(FORMATTER) : null)
                    .amount(invoice.amount())
                    .purpose(invoice.purpose())
                    .isInvoice(invoice.isInvoice())
                    .fileName(invoice.fileName())
                    .build();
        } catch (Exception e) {
            throw new DocHubAiException("Error mapping Invoice to DTO without file: " + e.getMessage());
        }
    }

    // data for chart
    public InvoiceDto toDtoForChart(Invoice invoice) {
        try {
            return InvoiceDto.builder()
                    .id(invoice.id())
                    .date(invoice.date() != null ? invoice.date().format(FORMATTER) : null)
                    .amount(invoice.amount())
                    .build();
        } catch (Exception e) {
            throw new DocHubAiException("Error mapping Invoice to DTO for chart: " + e.getMessage());
        }
    }
}
