package com.DemoRestApi.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

@Data
@SuperBuilder
@XmlRootElement
@AllArgsConstructor
@NoArgsConstructor
public class ReceiptCreateRequest {
    private String tokenKey;
    private String username;
    private String mobile;
    private String bankCode;
    private String accountNo;
    private Date payDate;
    private String additionalData;
    private Double debitAmount;
    private String orderCode;
    private Double realAmount;
    private String promotionCode;
    private Date createDate;

}
