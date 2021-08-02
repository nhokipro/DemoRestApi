package com.Producer.Service;

import com.Producer.ConnectRedis.ConnectionPoolRedis;
import com.Producer.Entity.Receipt;
import com.Producer.Repository.ReceiptRepository;
import com.Producer.RestReponseHandle.RestResponseHandle;
import com.Producer.Validation.ValidateReceipt;
import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.log4j.BasicConfigurator;
import redis.clients.jedis.Jedis;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.concurrent.TimeoutException;

@Slf4j
@AllArgsConstructor
@Path("/receipt")
public class ReceiptService {

    private ValidateReceipt validateReceipt;
    private ReceiptRepository receiptRepository;
    private Jedis jedis;
    private Gson gson;

    public ReceiptService() {
        this.validateReceipt = new ValidateReceipt();
        this.receiptRepository = new ReceiptRepository();
        this.jedis = ConnectionPoolRedis.getJedisPool().getResource();
        this.gson = new Gson();
    }

    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public RestResponseHandle pushReceipt(Receipt receipt) throws IOException, TimeoutException, InterruptedException, ParseException {
        BasicConfigurator.configure();
        log.info("start service pushReceipt");

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");

        validateReceipt.validateReceipt(receipt);

        RestResponseHandle response = receiptRepository.pushReceipt(receipt);

        if (response.getResCode() == 200) {
            log.info("save in redis");
            jedis.lpush("tokens", receipt.getTokenKey());
            jedis.expire("tokens", (simpleDateFormat.parse("23:59:59").getTime() -
                    simpleDateFormat.parse(simpleDateFormat.format(receipt.getCreateDate())).getTime()));
        }
        log.info("end service");
        return response;
    }
}