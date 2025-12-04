package org.example.backend.model.entities;

import lombok.*;
import org.springframework.data.annotation.Id;

/**
 * Vertrag
 */
@With
@Builder
public record Contract(
        @Id
        String id
) {
}
