package anh.pham.adapters.outbound.persistence;

import anh.pham.adapters.outbound.repository.PaymentHistoryRepository;
import anh.pham.domain.model.PaymentHistory;
import anh.pham.exception.InvalidInformation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PaymentHistoryPersistenceAdapterTest {
    List<PaymentHistory> mockHistory;
    @Mock
    PaymentHistoryRepository paymentHistoryRepository;
    @InjectMocks
    PaymentHistoryPersistenceAdapter paymentHistoryPersistenceAdapter;
    @BeforeEach
    public void setup() {
        mockHistory = new ArrayList<>();
        mockHistory.add(new PaymentHistory( "ELECTRIC", 100000, LocalDate.of(2024, 9, 2), "PROCESSED", "EVN_HCM", 1L));
        mockHistory.add(new PaymentHistory( "WATER", 200000, LocalDate.of(2024, 9, 15), "PROCESSED", "SAVACO HCMC", 2L));

    }

    @Test
    public void testSavePaymentHistories_whenAllValidBillIds_thenSaveHistories() {
        // Arrange
        // Act
        paymentHistoryPersistenceAdapter.saveHistory(mockHistory);
        // Assert
        verify(paymentHistoryRepository, times(1)).save(mockHistory);

    }

    @Test
    public void testSavePaymentHistories_whenHavingSomeInvalidBillIds_thenSaveHistories() {
        // Arrange
        mockHistory.add(new PaymentHistory( "WATER", 200000, LocalDate.of(2024, 9, 15), "PROCESSED", "SAVACO HCMC", null));
        // Act
        InvalidInformation thrown = assertThrows(
                InvalidInformation.class,
                () -> paymentHistoryPersistenceAdapter.saveHistory(mockHistory)
        );
        // Assert
        verify(paymentHistoryRepository, never()).save(mockHistory);
        assertEquals("Invalid bill ids !!!", thrown.getMessage());
    }

    @Test
    public void testSavePaymentHistories_whenHavingAllInvalidBillIds_thenDoNotSaveHistories() {
        // Arrange
        mockHistory = new ArrayList<>();
        mockHistory.add(new PaymentHistory( "ELECTRIC", 100000, LocalDate.of(2024, 9, 2), "PROCESSED", "EVN_HCM", null));
        mockHistory.add(new PaymentHistory( "WATER", 200000, LocalDate.of(2024, 9, 15), "PROCESSED", "SAVACO HCMC", null));
        // Act
        // Assert
        InvalidInformation thrown = assertThrows(InvalidInformation.class, () -> paymentHistoryPersistenceAdapter.saveHistory(mockHistory));
        verify(paymentHistoryRepository, never()).save(mockHistory);
        assertEquals("Invalid bill ids !!!", thrown.getMessage());
    }

    @Test
    public void testSavePaymentHistories_whenHavingNoBills_thenStillOperate() {
        // Arrange
        mockHistory = new ArrayList<>();
        // Act
        paymentHistoryPersistenceAdapter.saveHistory(mockHistory);
        // Assert
        verify(paymentHistoryRepository, times(1)).save(mockHistory);
    }

    @Test
    public void testFindAll_whenHavingBills_thenReturnAll() {
        // Arrange
        when(paymentHistoryRepository.findAll()).thenReturn(mockHistory);
        // Act
        List<PaymentHistory> paymentHistories = paymentHistoryPersistenceAdapter.findAll();
        // Assert
        verify(paymentHistoryRepository, times(1)).findAll();

        assertEquals(mockHistory.size(), paymentHistories.size());
        assertSame(mockHistory, paymentHistories);
    }
}
