package com.Producer.Exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ApiIdNullException extends RuntimeException {
    public ApiIdNullException() {
        super();
        log.info("apiId null Exception");
    }
}
