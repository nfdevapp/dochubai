package org.example.backend.model.dto;

import lombok.Builder;
import lombok.With;
import java.util.List;

@Builder
@With
public record ContractDto (
        String id,
        String title,
        String description,
        String startDate,
        String endDate,
        int aiLevel,
        String aiAnalysisText,
        String fileName,
        List<Integer> file // Frontend sendet Uint8Array => Array von Zahlen
) {
}