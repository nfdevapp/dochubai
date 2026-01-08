package org.example.backend.controller;

import lombok.RequiredArgsConstructor;
import org.example.backend.model.dto.ChatAiDto;
import org.example.backend.service.ChatGPTService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * KI-Assistent Chat
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chatai")
public class ChatAiController {

    private final ChatGPTService chatGPTService;

    @PostMapping("/question")
    public ResponseEntity<ChatAiDto> userQuestion(@RequestBody ChatAiDto request) {

        String answer = chatGPTService.askWithContext(request.userQuestion());

        ChatAiDto response = ChatAiDto.builder()
                .userQuestion(request.userQuestion())
                .aiAnswer(answer)
                .build();

        return ResponseEntity.ok(response);
    }
}
