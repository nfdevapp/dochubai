package org.example.backend.model.dto;

import lombok.Builder;
import lombok.With;

@Builder
@With
public record ContractDto (
        String id,
        String title,
        String description,
        String startDate,
        String endDate,
        String extractedText,
        int aiLevel,
        String aiAnalysisText,
        String fileName,
        //Muss umgewandelt werden: JSON = Base64, MongoDB = byte[]
        String fileBase64
) {
}