package org.example.backend.exeptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DocHubAiException.class)
    public ResponseEntity<ApiError> handleDocHubAiException(DocHubAiException ex) {
        //wird message und status geschickt als JSON-Fehlerobjekt
        ApiError error = new ApiError(ex.getMessage(), HttpStatus.NOT_FOUND.value());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(error);
    }
}
