package org.example.backend.model.entities;

import lombok.Builder;
import lombok.With;
import org.example.backend.utils.enums.PromptType;
import org.springframework.data.annotation.Id;
/**
 * Prompts
 */
@With
@Builder
public record ChatGPT(
        @Id
        String id,
        PromptType key,//WICHTIG muss key heißen => sonst findet MongoDB nicht
        String prompt
) {
}
