package org.example.backend.model.entities;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.format.annotation.DateTimeFormat;
import org.bson.types.Binary;

import java.time.LocalDate;

/**
 * Vertrag
 */
@With
@Builder
public record Contract(
        @Id
        String id,
        String name,
        String description,
        @DateTimeFormat(pattern = "dd.MM.yyyy")
        LocalDate startDate,
        @DateTimeFormat(pattern = "dd.MM.yyyy")
        LocalDate endDate,
        int aiLevel
//        ObjectId fileId // Referenz auf die Datei in GridFS
) {
}
