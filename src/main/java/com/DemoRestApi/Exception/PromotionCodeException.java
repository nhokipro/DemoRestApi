package com.DemoRestApi.Exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PromotionCodeException extends RuntimeException{
    public PromotionCodeException() {
        super();
        log.info("promotionCode Exception");
    }
}
