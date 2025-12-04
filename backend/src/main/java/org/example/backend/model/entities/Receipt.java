package org.example.backend.model.entities;

import lombok.*;
import org.springframework.data.annotation.Id;

/**
 * Zahlungsbeleg, Abrechnung
 */
@With
@Builder
public record Receipt(
        @Id
        String id,
        String name
) {
}
