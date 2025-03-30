package org.t1academy.tasktracker.exception;


import org.t1academy.tasktracker.exception.response.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({AppException.class})
    public ResponseEntity<ErrorResponse> handleException(AppException exception) {
        ErrorResponse errorResponse = ErrorResponse.builder().errorMessage(exception.getMessage()).timestamp(LocalDateTime.now()).build();
        return ResponseEntity.status(exception.getHttpStatus()).body(errorResponse);
    }
}
