package com.example.cloud.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ProfileNotFoundException extends RuntimeException {
    public ProfileNotFoundException(String message) {
        super(message);
    }
}
