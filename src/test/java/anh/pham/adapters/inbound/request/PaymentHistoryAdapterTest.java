package anh.pham.adapters.inbound.request;

import anh.pham.domain.model.PaymentHistory;
import anh.pham.ports.inbound.PaymentHistoryInboundPorts;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
public class PaymentHistoryAdapterTest {
    List<PaymentHistory> mockHistories;
    @Mock
    PaymentHistoryInboundPorts paymentInput;
    @InjectMocks
    PaymentHistoryRequestAdapter paymentHistoryRequestAdapter;

    @BeforeEach
    void setup() {
        mockHistories = Arrays.asList(
                new PaymentHistory( "ELECTRIC", 100000, LocalDate.of(2024, 9, 2), "PROCESSED", "EVN_HCM", 1L),
                new PaymentHistory( "WATER", 200000, LocalDate.of(2024, 9, 15), "PROCESSED", "SAVACO HCMC", 2L)
        );
    }

    @Test
    public void testViewAllHistories_whenHavingHistories_thenProcessToOtherLayer() {
        // Arrange

        // Act
        paymentHistoryRequestAdapter.viewAllPayments();

        // Assert
        verify(paymentInput, times(1)).findAll();

    }

    @Test
    public void testViewAllHistories_whenHavingNoHistory_thenProcessToOtherLayer() {
        // Arrange

        // Act
        paymentHistoryRequestAdapter.viewAllPayments();

        // Assert
        verify(paymentInput, times(1)).findAll();

    }
}
