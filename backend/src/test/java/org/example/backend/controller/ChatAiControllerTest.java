package org.example.backend.controller;

import org.example.backend.ai.ChatAiRequest;
import org.example.backend.model.dto.ChatAiDto;
import org.example.backend.service.ChatGPTService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ChatAiControllerTest {

    @Mock
    private ChatGPTService chatGPTService;

    private ChatAiController chatAiController;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        chatAiController = new ChatAiController(chatGPTService);
    }

    @Test
    void userQuestion_returnsExpectedResponse() {
        // Testdaten
        String userQuestion = "Wie ist der Status der Rechnungen?";
        List<ChatAiDto> history = List.of(
                new ChatAiDto("Vorherige Frage", "Vorherige Antwort")
        );

        ChatAiRequest request = new ChatAiRequest(history, userQuestion);

        // Erwartete Antwort vom Service
        String aiAnswer = "Antwort vom KI-Assistenten";

        when(chatGPTService.askWithContextWithHistory(history, userQuestion))
                .thenReturn(aiAnswer);

        // Aufruf der Controller-Methode
        var responseEntity = chatAiController.userQuestion(request);

        // Assertions
        assertNotNull(responseEntity);
        assertEquals(200, responseEntity.getStatusCodeValue());

        ChatAiDto body = responseEntity.getBody();
        assertNotNull(body);
        assertEquals(userQuestion, body.userQuestion());
        assertEquals(aiAnswer, body.aiAnswer());

        // Verifikation des Service-Aufrufs
        verify(chatGPTService, times(1)).askWithContextWithHistory(history, userQuestion);
    }

    @Test
    void userQuestion_handlesEmptyHistory() {
        String userQuestion = "Neue Frage ohne History";
        ChatAiRequest request = new ChatAiRequest(List.of(), userQuestion);

        String aiAnswer = "Antwort ohne History";
        when(chatGPTService.askWithContextWithHistory(List.of(), userQuestion)).thenReturn(aiAnswer);

        var responseEntity = chatAiController.userQuestion(request);

        assertNotNull(responseEntity);
        assertEquals(200, responseEntity.getStatusCodeValue());
        assertEquals(userQuestion, responseEntity.getBody().userQuestion());
        assertEquals(aiAnswer, responseEntity.getBody().aiAnswer());

        verify(chatGPTService, times(1)).askWithContextWithHistory(List.of(), userQuestion);
    }

    @Test
    void userQuestion_throwsIfServiceFails() {
        String userQuestion = "Fehlerfrage";
        ChatAiRequest request = new ChatAiRequest(List.of(), userQuestion);

        when(chatGPTService.askWithContextWithHistory(List.of(), userQuestion))
                .thenThrow(new RuntimeException("Service Error"));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> chatAiController.userQuestion(request));

        assertEquals("Service Error", ex.getMessage());

        verify(chatGPTService, times(1)).askWithContextWithHistory(List.of(), userQuestion);
    }
}
