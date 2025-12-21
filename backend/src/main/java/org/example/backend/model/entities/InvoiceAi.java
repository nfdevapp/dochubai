package org.example.backend.model.entities;

import lombok.Builder;
import lombok.With;
import org.springframework.data.annotation.Id;

/**
 * KI Text für die Rechnungen
 */
@With
@Builder
public record InvoiceAi(
        @Id
        String id,
        String aiAnalysisText
) {
}
