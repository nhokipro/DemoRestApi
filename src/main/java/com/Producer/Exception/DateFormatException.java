package com.Producer.Exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DateFormatException extends RuntimeException {
    public DateFormatException() {
        super();
        log.info("DateFormat Exception");
    }
}
