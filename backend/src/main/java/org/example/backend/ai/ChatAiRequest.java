package org.example.backend.ai;

import org.example.backend.model.dto.ChatAiDto;

import java.util.List;

public record ChatAiRequest(
        List<ChatAiDto> history,
        String userQuestion
) {}