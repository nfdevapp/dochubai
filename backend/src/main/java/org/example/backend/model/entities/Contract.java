package org.example.backend.model.entities;

import lombok.*;
import org.springframework.data.annotation.Id;

import java.time.LocalDate;

/**
 * Vertrag
 */
@With
@Builder
public record Contract(
        @Id
        String id,
        String title,
        String description,
        LocalDate startDate,
        LocalDate endDate,
        String extractedText,
        int aiLevel,
        String aiAnalysisText,
        String fileName,
        byte[] file
) {
}
