package org.example.backend.model.dto;

import lombok.Builder;
import lombok.With;

@Builder
@With
public record ChatAiDto(
        String userQuestion,
        String aiAnswer
) {

}
