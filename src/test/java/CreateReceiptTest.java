import com.DemoRestApi.Exception.DateFormatException;
import com.DemoRestApi.Exception.OrderCodeNullException;
import com.DemoRestApi.Exception.PromotionCodeException;
import com.DemoRestApi.Exception.RealAmountException;
import com.DemoRestApi.Entity.Receipt;
import com.DemoRestApi.DTO.ReceiptCreateRequest;
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
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class CreateReceiptTest {

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
        Receipt mockReceiptInDB = Receipt.builder()
                .id(1L)
                .tokenKey("1234")
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

//        when(receiptRepository.existByTokenAndCreateDate(anyString(), any())).thenReturn(false);

        ReceiptCreateRequest inputReceipt = ReceiptCreateRequest.builder()
                .tokenKey("1234")
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

        when(receiptRepository.createReceipt(inputReceipt)).thenReturn(mockReceiptInDB);

        Receipt receiptActual = receiptService.createReceipt(inputReceipt);

        Receipt receiptExpect = Receipt.builder()
                .id(1L)
                .tokenKey("1234")
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

        ReceiptCreateRequest inputReceipt = ReceiptCreateRequest.builder()
                .tokenKey("1234")
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

        when(receiptRepository.createReceipt(inputReceipt)).thenReturn(mockReceiptSaveInDB);

        Receipt receiptActual = receiptService.createReceipt(inputReceipt);

        Receipt receiptExpect = Receipt.builder()
                .id(1L)
                .tokenKey("1234")
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
    void createReceiptFailWhenPayDateException() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        Receipt mockReceiptSaveInDB = Receipt.builder()
                .id(1L)
                .tokenKey("1234")
                .username("abcxyz")
                .mobile("0123456789")
                .bankCode("970445")
                .accountNo("1")
                .payDate(new Date(2021 - 7 - 26))
                .additionalData("")
                .debitAmount(5000.0)
                .orderCode(null)
                .realAmount(4000.0)
                .promotionCode("abc")
                .createDate(simpleDateFormat.parse("2021726000000"))
                .build();


        ReceiptCreateRequest inputReceipt = ReceiptCreateRequest.builder()
                .tokenKey("1234")
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

        when(receiptRepository.createReceipt(inputReceipt)).thenReturn(mockReceiptSaveInDB);

        assertThrows(DateFormatException.class, () -> receiptService.createReceipt(inputReceipt));
    }

    @SneakyThrows
    @Test
    void createReceiptFailWhenOrderDateException() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        Receipt mockReceiptSaveInDB = Receipt.builder()
                .id(1L)
                .tokenKey("1234")
                .username("abcxyz")
                .mobile("0123456789")
                .bankCode("970445")
                .accountNo("1")
                .payDate(simpleDateFormat.parse("2021726000000"))
                .additionalData("")
                .debitAmount(5000.0)
                .orderCode(null)
                .realAmount(4000.0)
                .promotionCode("abc")
                .createDate(new Date(2021 - 7 - 26))
                .build();


        ReceiptCreateRequest inputReceipt = ReceiptCreateRequest.builder()
                .tokenKey("1234")
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

        when(receiptRepository.createReceipt(inputReceipt)).thenReturn(mockReceiptSaveInDB);

        assertThrows(DateFormatException.class, () -> receiptService.createReceipt(inputReceipt));
    }

    @SneakyThrows
    @Test
    void createReceiptFailWhenRealAmountThanDebitAmount() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        Receipt mockReceiptInDB = Receipt.builder()
                .id(1L)
                .tokenKey("1234")
                .username("abcxyz")
                .mobile("0123456789")
                .bankCode("970445")
                .accountNo("1")
                .payDate(simpleDateFormat.parse("2021726000000"))
                .additionalData("")
                .debitAmount(5000.0)
                .orderCode("abc")
                .realAmount(5100.0)
                .promotionCode("xyz")
                .createDate(simpleDateFormat.parse("2021726000000"))
                .build();


        ReceiptCreateRequest inputReceipt = ReceiptCreateRequest.builder()
                .tokenKey("1234")
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

        when(receiptRepository.createReceipt(inputReceipt)).thenReturn(mockReceiptInDB);

        assertThrows(RealAmountException.class, () -> receiptService.createReceipt(inputReceipt));
    }

    @SneakyThrows
    @Test
    void createReceiptFailWhenPromotionCodeNull() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        Receipt mockReceiptInDB = Receipt.builder()
                .id(1L)
                .tokenKey("1234")
                .username("abcxyz")
                .mobile("0123456789")
                .bankCode("970445")
                .accountNo("1")
                .payDate(simpleDateFormat.parse("2021726000000"))
                .additionalData("")
                .debitAmount(5000.0)
                .orderCode("abc")
                .realAmount(4000.0)
                .promotionCode(null)
                .createDate(simpleDateFormat.parse("2021726000000"))
                .build();


        ReceiptCreateRequest inputReceipt = ReceiptCreateRequest.builder()
                .tokenKey("1234")
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

        when(receiptRepository.createReceipt(inputReceipt)).thenReturn(mockReceiptInDB);

        assertThrows(PromotionCodeException.class, () -> receiptService.createReceipt(inputReceipt));
    }

    @SneakyThrows
    @Test
    void createReceiptFailWhenOrderCodeNull() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        Receipt mockReceiptInDB = Receipt.builder()
                .id(1L)
                .tokenKey("1234")
                .username("abcxyz")
                .mobile("0123456789")
                .bankCode("970445")
                .accountNo("1")
                .payDate(simpleDateFormat.parse("2021726000000"))
                .additionalData("")
                .debitAmount(5000.0)
                .orderCode(null)
                .realAmount(4000.0)
                .promotionCode("abc")
                .createDate(simpleDateFormat.parse("2021726000000"))
                .build();


        ReceiptCreateRequest inputReceipt = ReceiptCreateRequest.builder()
                .tokenKey("1234")
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

        when(receiptRepository.createReceipt(inputReceipt)).thenReturn(mockReceiptInDB);

        assertThrows(OrderCodeNullException.class, () -> receiptService.createReceipt(inputReceipt));
    }
}
