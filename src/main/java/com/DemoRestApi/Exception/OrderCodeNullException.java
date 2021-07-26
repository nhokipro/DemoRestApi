package com.DemoRestApi.Exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OrderCodeNullException extends RuntimeException {
    public OrderCodeNullException() {
        super();
        log.info("orderCode is null Exception");
    }
}
