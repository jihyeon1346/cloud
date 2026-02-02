package com.example.cloud.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UploadFailedException extends RuntimeException {
    public UploadFailedException(String message) {
        super(message);
    }
}
