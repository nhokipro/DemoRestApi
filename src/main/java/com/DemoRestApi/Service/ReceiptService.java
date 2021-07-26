package com.DemoRestApi.Service;

import com.DemoRestApi.DTO.ReceiptCreateRequest;
import com.DemoRestApi.Entity.Receipt;
import com.DemoRestApi.Repository.ReceiptRepository;
import lombok.AllArgsConstructor;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.sql.SQLException;
import java.util.List;

@AllArgsConstructor
@Path("/receipt")
public class ReceiptService {

    private ReceiptRepository repository;

    public ReceiptService() {
        this.repository = new ReceiptRepository();
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public List<Receipt> getListReceipts() {
        return repository.getListReceipts();
    }

    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Receipt createReceipt(ReceiptCreateRequest receiptCreateRequest) throws SQLException {
        return repository.createReceipt(receiptCreateRequest);
    }
}