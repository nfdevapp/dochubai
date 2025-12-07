package org.example.backend.exeptions;


/**
 * Wird message und status geschickt als JSON-Fehlerobjekt: status + message
 */
public record ApiError(
        String message,
        int status
) {
}
