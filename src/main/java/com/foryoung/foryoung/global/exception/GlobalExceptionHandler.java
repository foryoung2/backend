package com.foryoung.foryoung.global.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(CustomException e) {

        ErrorCode errorCode = e.getErrorCode();

        return ResponseEntity
                .status(errorCode.getStatus())
                .body(
                        ErrorResponse.builder()
                                .code(errorCode.getCode())
                                .message(errorCode.getMessage())
                                .status(errorCode.getStatus().value())
                                .timestamp(LocalDateTime.now())
                                .build()
                );

    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException e) {

        String message = e.getBindingResult().getFieldError().getDefaultMessage();

        return ResponseEntity.badRequest()
                .body(
                        ErrorResponse.builder()
                                .code("VALIDATION_ERROR")
                                .message(message)
                                .status(400)
                                .timestamp(LocalDateTime.now())
                                .build()
                );

    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {

        return ResponseEntity.internalServerError()
                .body(
                        ErrorResponse.builder()
                                .code("INTERNAL_SERVER_ERROR")
                                .message(e.getMessage())
                                .status(500)
                                .timestamp(LocalDateTime.now())
                                .build()
                );
    }


}