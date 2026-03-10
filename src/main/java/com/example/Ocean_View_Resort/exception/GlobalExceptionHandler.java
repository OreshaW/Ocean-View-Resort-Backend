package com.example.Ocean_View_Resort.exception;

import org.springframework.core.NestedExceptionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> handleResourceNotFoundException(ResourceNotFoundException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("message", ex.getMessage());

        return ResponseEntity.status(404).body(error);
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<?> handleMaxUploadSizeExceededException(MaxUploadSizeExceededException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("message", "Upload too large. Reduce image sizes or increase multipart limits (spring.servlet.multipart.max-file-size/max-request-size).");

        Throwable root = NestedExceptionUtils.getMostSpecificCause(ex);
        if (root != null && root.getMessage() != null && !root.getMessage().isBlank()) {
            error.put("detail", root.getMessage());
        }

        return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body(error);
    }

    @ExceptionHandler(MultipartException.class)
    public ResponseEntity<?> handleMultipartException(MultipartException ex) {
        Map<String, String> error = new HashMap<>();

        Throwable root = NestedExceptionUtils.getMostSpecificCause(ex);
        String detail = root != null ? root.getMessage() : null;
        String detailLower = detail != null ? detail.toLowerCase() : "";

        boolean looksLikeSizeIssue = detailLower.contains("size")
                || detailLower.contains("too large")
                || detailLower.contains("exceed");

        if (detailLower.contains("boundary")) {
            error.put("message", "Invalid multipart request (missing boundary). If you're using FormData in the frontend, do not manually set the Content-Type header; let the browser/axios set it.");
        } else if (looksLikeSizeIssue) {
            error.put("message", "Upload too large. Reduce image sizes or increase multipart limits (spring.servlet.multipart.max-file-size/max-request-size).");
        } else {
            error.put("message", ex.getMessage());
        }

        if (detail != null && !detail.isBlank() && (ex.getMessage() == null || !detail.equals(ex.getMessage()))) {
            error.put("detail", detail);
        }

        return ResponseEntity.status(looksLikeSizeIssue ? HttpStatus.PAYLOAD_TOO_LARGE : HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> handleRuntimeException(RuntimeException ex) {

        Map<String, String> error = new HashMap<>();
        error.put("message", ex.getMessage());

        return ResponseEntity.badRequest().body(error);
    }
}
