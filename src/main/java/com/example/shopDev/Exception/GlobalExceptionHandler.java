package com.example.shopDev.Exception;

import com.example.shopDev.DTO.ErrorObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler({ConflictRequestError.class})
    public ResponseEntity<Object> handleConflictRequestError(ConflictRequestError exception) {
        ErrorObject errorEntity = ErrorObject.builder()
                .code(exception.getCode())
                .status(exception.getStatus())
                .message(exception.getMessage())
                .build();

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(errorEntity);
    }

    @ExceptionHandler({InvalidAPIKeyException.class})
    public ResponseEntity<Object> handleAPIKeyRequestError(InvalidAPIKeyException exception) {
        ErrorObject errorEntity = ErrorObject.builder()
                .code(exception.getCode())
                .status(exception.getStatus())
                .message(exception.getMessage())
                .build();

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errorEntity);
    }

    @ExceptionHandler({ForbidenError.class})
    public ResponseEntity<Object> handleForbidenError(ForbidenError exception) {
        ErrorObject errorEntity = ErrorObject.builder()
                .code(exception.getCode())
                .status(exception.getStatus())
                .message(exception.getMessage())
                .build();

        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(errorEntity);
    }




    @ExceptionHandler({AuthFailError.class})
    public ResponseEntity<Object> handleAPIKeyRequestError(AuthFailError exception) {
        ErrorObject errorEntity = ErrorObject.builder()
                .code(exception.getCode())
                .status(exception.getStatus())
                .message(exception.getMessage())
                .build();

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(errorEntity);
    }

    @ExceptionHandler({BadRequestError.class})
    public ResponseEntity<Object> handleBadRequestError(BadRequestError exception) {
        ErrorObject errorEntity = ErrorObject.builder()
                .code(exception.getCode())
                .status(exception.getStatus())
                .message(exception.getMessage())
                .build();

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errorEntity);
    }

    @ExceptionHandler({CreationException.class})
    public ResponseEntity<Object> handleCreationException(CreationException exception) {
        ErrorObject errorEntity = ErrorObject.builder()
                .code(exception.getCode())
                .status(exception.getStatus())
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
