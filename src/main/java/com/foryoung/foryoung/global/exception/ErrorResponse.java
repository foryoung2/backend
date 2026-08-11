package com.foryoung.foryoung.global.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class ErrorResponse {


    private String code;

    private String message;

    private int status;

    private LocalDateTime timestamp;


}