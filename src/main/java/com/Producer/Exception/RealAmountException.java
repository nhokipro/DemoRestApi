package com.Producer.Exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RealAmountException extends RuntimeException {
    public RealAmountException() {
        super();
        log.info("RealAmount Exception");
    }
}
