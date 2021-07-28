package com.DemoRestApi.Service;

import com.DemoRestApi.Entity.Receipt;
import com.DemoRestApi.Exception.*;
import com.DemoRestApi.Repository.ReceiptRepository;
import lombok.AllArgsConstructor;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.concurrent.TimeoutException;

@AllArgsConstructor
@Path("/receipt")
public class ReceiptService {

    private ReceiptRepository receiptRepository;

    public ReceiptService() {
        this.receiptRepository = new ReceiptRepository();
    }

    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Receipt pushReceipt(Receipt receipt) throws IOException, TimeoutException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        simpleDateFormat.setLenient(false);
        if (receiptRepository.existByTokenAndCreateDate(receipt.getTokenKey(), (Date) receipt.getCreateDate())) {
            throw new TokenExistException();
        }
        if (receipt.getApiID() == null) {
            throw new ApiIdNullException();
        }
        if (receipt.getOrderCode() == null) {
            throw new OrderCodeNullException();
        }
        if (receipt.getRealAmount() > receipt.getDebitAmount()) {
            throw new RealAmountException();
        }
        if (receipt.getRealAmount() != receipt.getDebitAmount() && receipt.getPromotionCode() == null) {
            throw new PromotionCodeException();
        }
//        String s = simpleDateFormat.format(receipt.getPayDate());
//        if (!GenericValidator.isDate(simpleDateFormat.format(receipt.getPayDate()), "yyyyMMddHHmmss", true)) {
//            throw new DateFormatException();
//        }
        return receiptRepository.pushReceipt(receipt);
    }
}