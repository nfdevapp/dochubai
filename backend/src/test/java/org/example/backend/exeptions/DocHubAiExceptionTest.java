package org.example.backend.exeptions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DocHubAiExceptionTest {

    @Test
    void constructor_withMessage_setsMessage() {
        String message = "Test message";
        DocHubAiException ex = new DocHubAiException(message);

        assertEquals(message, ex.getMessage());
        assertNull(ex.getCause());
    }

    @Test
    void constructor_withMessageAndCause_setsBoth() {
        String message = "Test message";
        Throwable cause = new RuntimeException("Cause");
        DocHubAiException ex = new DocHubAiException(message, cause);

        assertEquals(message, ex.getMessage());
        assertEquals(cause, ex.getCause());
    }

    @Test
    void exceptionIsRuntimeException() {
        DocHubAiException ex = new DocHubAiException("Test");
        assertTrue(ex instanceof RuntimeException);
    }
}
