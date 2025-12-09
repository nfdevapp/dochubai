package org.example.backend.model.entities;

import lombok.*;
import org.springframework.data.annotation.Id;

/**
 * Rechnung
 */
@With
@Builder
public record Statement(
        @Id
        String id
) {
}
