package com.Producer.Validation;

import com.Producer.Entity.Receipt;
import com.Producer.Exception.*;
import com.Producer.Repository.ReceiptRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.validator.GenericValidator;

@Slf4j
public class ValidateReceipt {
    public void validateReceipt(Receipt receipt) {
        log.info("start validateReceipt");
        ReceiptRepository receiptRepository = new ReceiptRepository();

        log.info("check existByTokenAndCreateDate");
        if (receiptRepository.existByTokenAndCreateDate(receipt.getTokenKey())) {
            throw new TokenExistException();
        }
        log.info("check apiID null");
        if (receipt.getApiID() == null) {
            throw new ApiIdNullException();
        }
        log.info("check orderCode null");
        if (receipt.getOrderCode() == null) {
            throw new OrderCodeNullException();
        }
        log.info("check realAmount and debitAmount");
        if (receipt.getRealAmount() > receipt.getDebitAmount()) {
            throw new RealAmountException();
        }
        log.info("check promotionCode null");
        if (receipt.getRealAmount() != receipt.getDebitAmount() && receipt.getPromotionCode() == null) {
            throw new PromotionCodeException();
        }
        log.info("check format payDate");
        if (receipt.getPayDate() == null || receipt.getPayDate().trim().equals("") ||
                !GenericValidator.isDate(receipt.getPayDate(), "yyyyMMddHHmmss", true)) {
            throw new DateFormatException();
        }
        log.info("end validateReceipt");
    }
}
