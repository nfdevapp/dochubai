package org.example.backend.model.dto;

import lombok.Builder;
import lombok.With;

@Builder
@With
public record InvoiceDto(
        String id,
        String docNumber,
        String date,
        double amount,
        String purpose,
        boolean isInvoice,
        String fileName,
        //Muss umgewandelt werden: JSON = Base64, MongoDB = byte[]
        String fileBase64


) {
}
