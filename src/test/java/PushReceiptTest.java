import com.DemoRestApi.Entity.Receipt;
import com.DemoRestApi.Entity.RestResponseHandle;
import com.DemoRestApi.Exception.*;
import com.DemoRestApi.Repository.ReceiptRepository;
import com.DemoRestApi.Service.ReceiptService;
import com.google.gson.Gson;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeoutException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class PushReceiptTest {

    @Mock
    private ReceiptRepository receiptRepository;

    @Mock
    private Jedis jedis;

    private ReceiptService receiptService;
    private Gson gson;

    @BeforeEach
    public void setUp() {
        receiptService = new ReceiptService(
                receiptRepository,
                jedis,
                gson
        );
    }

    @Test
    void createReceiptSuccess() throws ParseException, IOException, InterruptedException, TimeoutException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");

        when(receiptRepository.existByTokenAndCreateDate(anyString(), any())).thenReturn(false);

        Receipt inputReceipt = Receipt.builder()
                .tokenKey("1234")
                .apiID("rest")
                .username("abcxyz")
                .mobile("0123456789")
                .bankCode("970445")
                .accountNo("1")
                .payDate("20210726000000")
                .additionalData("")
                .debitAmount(5000.0)
                .orderCode("abc")
                .realAmount(4800.0)
                .promotionCode("xyz")
                .createDate(new Date())
                .build();

        RestResponseHandle mockRestResponse = RestResponseHandle.builder()
                .resCode(200)
                .respDesc("Success")
                .build();

        when(receiptRepository.pushReceipt(inputReceipt)).thenReturn(mockRestResponse);


        RestResponseHandle actual = receiptService.pushReceipt(inputReceipt);

        RestResponseHandle expect = RestResponseHandle.builder()
                .resCode(200)
                .respDesc("Success")
                .build();

        assertEquals(expect, actual);
    }

    @SneakyThrows
    @Test
    void createReceiptSuccessWhenTokenExistsDifferentDate() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");

        when(receiptRepository.existByTokenAndCreateDate(anyString(), any())).thenReturn(false);

        Receipt inputReceipt = Receipt.builder()
                .tokenKey("1234")
                .apiID("rest")
                .username("abcxyz")
                .mobile("0123456789")
                .bankCode("970445")
                .accountNo("1")
                .payDate("20210726000000")
                .additionalData("")
                .debitAmount(5000.0)
                .orderCode("abc")
                .realAmount(4800.0)
                .promotionCode("xyz")
                .createDate(new Date())
                .build();

        RestResponseHandle mockRestResponse = RestResponseHandle.builder()
                .resCode(200)
                .respDesc("Success")
                .build();

        when(receiptRepository.pushReceipt(inputReceipt)).thenReturn(mockRestResponse);

        RestResponseHandle actual = receiptService.pushReceipt(inputReceipt);

        RestResponseHandle expect = RestResponseHandle.builder()
                .resCode(200)
                .respDesc("Success")
                .build();

        assertEquals(expect, actual);
    }

    @Test
    void createReceiptFailWhenTokenExists() {

        when(receiptRepository.existByTokenAndCreateDate(anyString(), any())).thenReturn(true);

        Receipt inputReceipt = Receipt.builder()
                .tokenKey("1234")
                .apiID("rest")
                .username("abcxyz")
                .mobile("0123456789")
                .bankCode("970445")
                .accountNo("1")
                .additionalData("")
                .debitAmount(5000.0)
                .orderCode("abc")
                .realAmount(4800.0)
                .promotionCode("xyz")
                .build();

        assertThrows(TokenExistException.class, () -> receiptService.pushReceipt(inputReceipt));
    }

    @SneakyThrows
    @Test
    void createReceiptFailWhenApiIdNull() {
        when(receiptRepository.existByTokenAndCreateDate(anyString(), any())).thenReturn(false);

        Receipt inputReceipt = Receipt.builder()
                .tokenKey("1234")
                .apiID(null)
                .username("abcxyz")
                .mobile("0123456789")
                .bankCode("970445")
                .accountNo("1")
                .additionalData("")
                .debitAmount(5000.0)
                .orderCode("abc")
                .realAmount(4000.0)
                .promotionCode("abc")
                .build();

        assertThrows(ApiIdNullException.class, () -> receiptService.pushReceipt(inputReceipt));
    }

    @SneakyThrows
    @Test
    void createReceiptFailWhenPayDateIsNotFormat() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
        simpleDateFormat.setLenient(false);

        when(receiptRepository.existByTokenAndCreateDate(anyString(), any())).thenReturn(false);

        Receipt inputReceipt = Receipt.builder()
                .tokenKey("1234")
                .apiID("rest")
                .username("abcxyz")
                .mobile("0123456789")
                .bankCode("970445")
                .accountNo("1")
                .payDate("2021/11/30")
                .additionalData("")
                .debitAmount(5000.0)
                .orderCode("xyz")
                .realAmount(4000.0)
                .promotionCode("abc")
                .build();

        assertThrows(DateFormatException.class, () -> receiptService.pushReceipt(inputReceipt));
    }

    @SneakyThrows
    @Test
    void createReceiptFailWhenOrderCodeNull() {
        when(receiptRepository.existByTokenAndCreateDate(anyString(), any())).thenReturn(false);

        Receipt inputReceipt = Receipt.builder()
                .tokenKey("1234")
                .apiID("rest")
                .username("abcxyz")
                .mobile("0123456789")
                .bankCode("970445")
                .accountNo("1")
                .additionalData("")
                .debitAmount(5000.0)
                .orderCode(null)
                .realAmount(4000.0)
                .promotionCode("abc")
                .build();

        assertThrows(OrderCodeNullException.class, () -> receiptService.pushReceipt(inputReceipt));
    }

    @SneakyThrows
    @Test
    void createReceiptFailWhenRealAmountThanDebitAmount() {
        when(receiptRepository.existByTokenAndCreateDate(anyString(), any())).thenReturn(false);

        Receipt inputReceipt = Receipt.builder()
                .tokenKey("1234")
                .apiID("rest")
                .username("abcxyz")
                .mobile("0123456789")
                .bankCode("970445")
                .accountNo("1")
                .additionalData("")
                .debitAmount(5000.0)
                .orderCode("abc")
                .realAmount(5100.0)
                .promotionCode("xyz")
                .build();

        assertThrows(RealAmountException.class, () -> receiptService.pushReceipt(inputReceipt));
    }

    @SneakyThrows
    @Test
    void createReceiptFailWhenPromotionCodeNull() {
        when(receiptRepository.existByTokenAndCreateDate(anyString(), any())).thenReturn(false);

        Receipt inputReceipt = Receipt.builder()
                .tokenKey("1234")
                .apiID("rest")
                .username("abcxyz")
                .mobile("0123456789")
                .bankCode("970445")
                .accountNo("1")
                .additionalData("")
                .debitAmount(5000.0)
                .orderCode("abc")
                .realAmount(4000.0)
                .promotionCode(null)
                .build();

        assertThrows(PromotionCodeException.class, () -> receiptService.pushReceipt(inputReceipt));
    }

}
