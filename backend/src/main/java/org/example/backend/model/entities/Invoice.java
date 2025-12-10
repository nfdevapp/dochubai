package org.example.backend.model.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.data.annotation.Id;

import java.time.LocalDate;

/**
 * Abrechnungen
 */
@With
@Builder
public record Invoice(
        @Id
        String id,
        String docNumber,//Belegnummer
        LocalDate date,
        double amount,//Preis
        String purpose,//Verwendungszweck
        boolean isInvoice,//Rechnung oder Zahlungsbeleg
        String fileName,//Dateiname
        byte[] file//File in base64
) {
}
