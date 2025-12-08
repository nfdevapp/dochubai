package org.example.backend.model.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
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
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy")
        LocalDate startDate,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy")
        LocalDate endDate,
        int aiLevel,
        String aiAnalysisText,
        String fileName,
        byte[] file
) {
}
