package com.foryoung.foryoung.global.exception;

public class ElasticsearchOperationException extends RuntimeException {


    public ElasticsearchOperationException(String message, Throwable cause) {
        super(message, cause);
    }


}