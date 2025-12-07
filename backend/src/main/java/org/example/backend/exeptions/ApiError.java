package org.example.backend.exeptions;


/**
 * Wird message und status geschickt als JSON-Fehlerobjekt
 */
public record ApiError(
        String message,
        int status
) {
}
