package com.Producer.Exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TokenExistException extends RuntimeException {
    public TokenExistException() {
        super();
        log.info("token exists Exception");
    }
}
