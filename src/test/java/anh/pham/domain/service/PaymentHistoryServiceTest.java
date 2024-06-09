package anh.pham.domain.service;

import anh.pham.domain.model.Bill;
import anh.pham.domain.model.PaymentHistory;
import anh.pham.ports.outbound.PaymentHistoryOutboundPorts;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PaymentHistoryServiceTest {
    Set<Bill> mockBills;
    List<PaymentHistory> mockHistory;
    @InjectMocks
    PaymentHistoryService paymentHistoryService;
    @Mock
    PaymentHistoryOutboundPorts paymentHistoryOutput;

    @BeforeEach
    public void setup() {
        mockBills = new HashSet<>(Arrays.asList(
                new Bill(1L, "ELECTRIC", 100_000, LocalDate.of(2024, 9, 2), "NOT_PAID", "EVN_HCM", 1L),
                new Bill(2L, "WATER", 200_000, LocalDate.of(2024, 9, 15), "NOT_PAID", "SAVACO HCMC", 1L)
        ));

        mockHistory = new ArrayList<>();
    }

    @Test
    public void testSaveHistory_whenBillsNormalSizedSet_thenSaveHistoryThoseBillsAsProcessed() {

        // Arrange
        List<PaymentHistory> expectHistories = Arrays.asList(
                new PaymentHistory( "ELECTRIC", 100_000, LocalDate.of(2024, 9, 2), "PROCESSED", "EVN_HCM", 1L),
                new PaymentHistory( "WATER", 200_000, LocalDate.of(2024, 9, 15), "PROCESSED", "SAVACO HCMC", 2L)
        );

        // Act
        paymentHistoryService.saveHistory(mockBills);

        // Assert
        ArgumentCaptor<List<PaymentHistory>> argumentCaptor = ArgumentCaptor.forClass(List.class);
        verify(paymentHistoryOutput).saveHistory(argumentCaptor.capture());
        List<PaymentHistory> capturedHistories = argumentCaptor.getValue();
        assertEquals(expectHistories.get(0).toString(), capturedHistories.get(0).toString());
        assertEquals(expectHistories.get(1).toString(), capturedHistories.get(1).toString());
    }

    @Test
    public void testSaveHistory_whenBillsEmpty_thenHistoryEmpty() {
        // Arrange
        mockBills = new HashSet<>();

        // Act
        paymentHistoryService.saveHistory(mockBills);

        // Assert
        ArgumentCaptor<List<PaymentHistory>> argumentCaptor = ArgumentCaptor.forClass(List.class);
        verify(paymentHistoryOutput).saveHistory(argumentCaptor.capture());
        List<PaymentHistory> capturedHistories = argumentCaptor.getValue();
        assertEquals(0, capturedHistories.size());
    }

    @Test
    public void testFindAll_whenHavingHistory_thenReturnAll() {
        // Arrange
        List<PaymentHistory> histories = Arrays.asList(
                new PaymentHistory( "ELECTRIC", 100_000, LocalDate.of(2024, 9, 2), "PROCESSED", "EVN_HCM", 1L),
                new PaymentHistory( "WATER", 200_000, LocalDate.of(2024, 9, 15), "PROCESSED", "SAVACO HCMC", 2L)
        );
        when(paymentHistoryOutput.findAll()).thenReturn(histories);

        // Act
        List<PaymentHistory> paymentHistories = paymentHistoryService.findAll();

        // Assert
        verify(paymentHistoryOutput, times(1)).findAll();
        assertEquals(histories.size(), paymentHistories.size());
    }

    @Test
    public void testFindAll_whenHavingNoHistory_thenReturnSize0() {
        // Arrange
        List<PaymentHistory> histories = Collections.emptyList();
        when(paymentHistoryOutput.findAll()).thenReturn(histories);

        // Act
        List<PaymentHistory> paymentHistories = paymentHistoryService.findAll();

        // Assert
        verify(paymentHistoryOutput, times(1)).findAll();
        assertEquals(0, paymentHistories.size());
    }
}
