package com.example.shopDev.Exception;

import com.example.shopDev.DTO.ErrorObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler({ConflictRequestError.class})
    public ResponseEntity<Object> handleConflictRequestError(ConflictRequestError exception) {
        ErrorObject errorEntity = ErrorObject.builder()
                .code(exception.getCode())
                .status("error")
                .message(exception.getMessage())
                .build();

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(exception.getMessage());
    }
    @ExceptionHandler({BadRequestError.class})
    public ResponseEntity<Object> handleBadRequestError(BadRequestError exception) {
        ErrorObject errorEntity = ErrorObject.builder()
                .code(exception.getCode())
                .status("error")
                .message(exception.getMessage())
                .build();

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errorEntity);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleUnwantedException(Exception e) {
        // Log lỗi ra và ẩn đi message thực sự
        e.printStackTrace();  // Thực tế người ta dùng logger
        return ResponseEntity.status(500).body("Unknown error");
    }
}
