package org.example.backend.model.entities;

import lombok.*;
import org.springframework.data.annotation.Id;

/**
 * Rechnung
 */
@With
@Builder
public record Invoice(
        @Id
        String id
) {
}
