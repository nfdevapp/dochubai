package org.example.backend.utils.mapper;

import org.example.backend.model.dto.InvoiceDto;
import org.example.backend.model.entities.Invoice;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

public class InvoiceMapper {
    //String to LocalDatetime or LocalDate to String
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    public static InvoiceDto toDto(Invoice invoice) {

        //JSON unterstützt fileBase64 und mongo db verlangt byte[]
        //byte[] to Base64
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
    }

    public static Invoice fromDto(InvoiceDto dto) {
        byte[] fileBytes = null;

        // Base64-String in byte[] umwandeln für Mongo DB
        // Base64 to byte[]
        if (dto.fileBase64() != null) {
            fileBytes = Base64.getDecoder().decode(dto.fileBase64());
        }

        return Invoice.builder()
                .docNumber(dto.docNumber())
                .date(dto.date() != null ? LocalDate.parse(dto.date(), FORMATTER) : null)
                .amount(dto.amount())
                .purpose(dto.purpose())
                .isInvoice(dto.isInvoice())
                .fileName(dto.fileName())
                .file(fileBytes)
                .build();
    }

    // getAllInvoices => without file and fileName
    public static InvoiceDto toDtoWithoutFile(Invoice invoice) {
        return InvoiceDto.builder()
                .id(invoice.id())
                .docNumber(invoice.docNumber())
                .amount(invoice.amount())
                .purpose(invoice.purpose())
                .isInvoice(invoice.isInvoice())
                .build();
    }
}
