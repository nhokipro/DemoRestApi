import com.DemoRestApi.Entity.Receipt;
import com.DemoRestApi.Exception.*;
import com.DemoRestApi.Repository.ReceiptRepository;
import com.DemoRestApi.Service.ReceiptService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.text.SimpleDateFormat;

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

    private ReceiptService receiptService;

    @BeforeEach
    public void setUp() {
        receiptService = new ReceiptService(
                receiptRepository
        );
    }

    @SneakyThrows
    @Test
    void createReceiptSuccess() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        Receipt mockReceipt = Receipt.builder()
                .id(1L)
                .tokenKey("1234")
                .apiID("rest")
                .username("abcxyz")
                .mobile("0123456789")
                .bankCode("970445")
                .accountNo("1")
                .payDate(simpleDateFormat.parse("2021726000000"))
                .additionalData("")
                .debitAmount(5000.0)
                .orderCode("abc")
                .realAmount(4800.0)
                .promotionCode("xyz")
                .createDate(simpleDateFormat.parse("2021726000000"))
                .build();

        when(receiptRepository.existByTokenAndCreateDate(anyString(), any())).thenReturn(false);

        Receipt inputReceipt = Receipt.builder()
                .tokenKey("1234")
                .apiID("rest")
                .username("abcxyz")
                .mobile("0123456789")
                .bankCode("970445")
                .accountNo("1")
                .payDate(simpleDateFormat.parse("2021726000000"))
                .additionalData("")
                .debitAmount(5000.0)
                .orderCode("abc")
                .realAmount(4800.0)
                .promotionCode("xyz")
                .build();

        when(receiptRepository.pushReceipt(any(Receipt.class))).thenReturn(mockReceipt);

        Receipt receiptActual = receiptService.pushReceipt(inputReceipt);

        Receipt receiptExpect = Receipt.builder()
                .id(1L)
                .tokenKey("1234")
                .apiID("rest")
                .username("abcxyz")
                .mobile("0123456789")
                .bankCode("970445")
                .accountNo("1")
                .payDate(simpleDateFormat.parse("2021726000000"))
                .additionalData("")
                .debitAmount(5000.0)
                .orderCode("abc")
                .realAmount(4800.0)
                .promotionCode("xyz")
                .createDate(simpleDateFormat.parse("2021726000000"))
                .build();

        assertEquals(receiptExpect, receiptActual);
    }

    @SneakyThrows
    @Test
    void createReceiptSuccessWhenTokenExistsDifferentDate() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        Receipt mockReceiptInDB = Receipt.builder()
                .id(1L)
                .tokenKey("1234")
                .apiID("rest")
                .username("abcxyz")
                .mobile("0123456789")
                .bankCode("970445")
                .accountNo("1")
                .payDate(simpleDateFormat.parse("2021725000000"))
                .additionalData("")
                .debitAmount(5000.0)
                .orderCode("abc")
                .realAmount(4800.0)
                .promotionCode("xyz")
                .createDate(simpleDateFormat.parse("2021725000000"))
                .build();


        Receipt mockReceiptSaveInDB = Receipt.builder()
                .id(1L)
                .tokenKey("1234")
                .apiID("rest")
                .username("abcxyz")
                .mobile("0123456789")
                .bankCode("970445")
                .accountNo("1")
                .payDate(simpleDateFormat.parse("2021726000000"))
                .additionalData("")
                .debitAmount(5000.0)
                .orderCode("abc")
                .realAmount(4800.0)
                .promotionCode("xyz")
                .createDate(simpleDateFormat.parse("2021726000000"))
                .build();

        when(receiptRepository.existByTokenAndCreateDate(anyString(), any())).thenReturn(false);

        Receipt inputReceipt = Receipt.builder()
                .tokenKey("1234")
                .apiID("rest")
                .username("abcxyz")
                .mobile("0123456789")
                .bankCode("970445")
                .accountNo("1")
                .payDate(simpleDateFormat.parse("2021726000000"))
                .additionalData("")
                .debitAmount(5000.0)
                .orderCode("abc")
                .realAmount(4800.0)
                .promotionCode("xyz")
                .build();

        when(receiptRepository.pushReceipt(inputReceipt)).thenReturn(mockReceiptSaveInDB);

        Receipt receiptActual = receiptService.pushReceipt(inputReceipt);

        Receipt receiptExpect = Receipt.builder()
                .id(1L)
                .tokenKey("1234")
                .apiID("rest")
                .username("abcxyz")
                .mobile("0123456789")
                .bankCode("970445")
                .accountNo("1")
                .payDate(simpleDateFormat.parse("2021726000000"))
                .additionalData("")
                .debitAmount(5000.0)
                .orderCode("abc")
                .realAmount(4800.0)
                .promotionCode("xyz")
                .createDate(simpleDateFormat.parse("2021726000000"))
                .build();

        assertEquals(receiptExpect, receiptActual);
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
                .payDate(simpleDateFormat.parse("2021/07/27"))
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
