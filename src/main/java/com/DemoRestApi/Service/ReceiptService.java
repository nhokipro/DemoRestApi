package com.DemoRestApi.Service;

import com.DemoRestApi.ConnectRedis.ConnectionPoolRedis;
import com.DemoRestApi.Entity.Receipt;
import com.DemoRestApi.Entity.RestResponseHandle;
import com.DemoRestApi.Exception.*;
import com.DemoRestApi.Repository.ReceiptRepository;
import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.validator.GenericValidator;
import org.apache.log4j.BasicConfigurator;
import redis.clients.jedis.Jedis;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.concurrent.TimeoutException;

@Slf4j
@AllArgsConstructor
@Path("/receipt")
public class ReceiptService {

    private ReceiptRepository receiptRepository;

    private Jedis jedis;

    private Gson gson;

    public ReceiptService() {
        this.receiptRepository = new ReceiptRepository();
        this.jedis = ConnectionPoolRedis.getJedisPool().getResource();
        this.gson = new Gson();
    }

    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public RestResponseHandle pushReceipt(Receipt receipt) throws IOException, TimeoutException, InterruptedException {
        BasicConfigurator.configure();
        log.info("start service pushReceipt");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        simpleDateFormat.setLenient(false);
        log.info("check existByTokenAndCreateDate");
        if (receiptRepository.existByTokenAndCreateDate(receipt.getTokenKey(), receipt.getCreateDate())) {
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
        log.info("save in redis");
        jedis.lpush("tokens", receipt.getTokenKey());
        jedis.lpush("createDates", simpleDateFormat.format(receipt.getCreateDate()));
        return receiptRepository.pushReceipt(receipt);
    }
}