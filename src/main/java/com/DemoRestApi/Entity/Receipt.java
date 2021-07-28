package com.DemoRestApi.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.json.JSONObject;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement
public class Receipt {

    private Long id;
    private String tokenKey;
    private String apiID;
    private String mobile;
    private String bankCode;
    private String accountNo;
    private Date payDate;
    private String additionalData;
    private Double debitAmount;
    private String respCode;
    private String respDesc;
    private String traceTransfer;
    private String messageType;
    private String checkSum;
    private String orderCode;
    private String username;
    private Double realAmount;
    private String promotionCode;
    private JSONObject addValue;
    private Date createDate;
}
