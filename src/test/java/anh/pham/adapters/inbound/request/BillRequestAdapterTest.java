package anh.pham.adapters.inbound.request;

import anh.pham.domain.model.Bill;
import anh.pham.domain.model.User;
import anh.pham.exception.InvalidInformation;
import anh.pham.ports.inbound.BillInboundPorts;
import anh.pham.ports.inbound.PaymentHistoryInboundPorts;
import anh.pham.ports.inbound.UserInboundPorts;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BillRequestAdapterTest {
    User user;
    Set<Bill> mockBills;
    Set<Bill> mockBillsPool;
    @InjectMocks
    BillRequestAdapter billRequestAdapter;
    @Mock
    BillInboundPorts billInput;
    @Mock
    PaymentHistoryInboundPorts paymentHistoryInput;
    @Mock
    UserInboundPorts userInput;

    private ByteArrayInputStream testIn;
    private ByteArrayOutputStream testOut;
    private void provideInput(String data) {
        testIn = new ByteArrayInputStream(data.getBytes());
        System.setIn(testIn);
    }
    private void provideOutput() {
        testOut = new ByteArrayOutputStream();

        System.setOut(new PrintStream(testOut));
    }

    @BeforeEach
    void setup() {
        user = new User(1L, "anh pham", 100_000);
        mockBills = new HashSet<>(Arrays.asList(
                new Bill(1L, "ELECTRIC", 100_000, LocalDate.of(2024, 9, 2), "NOT_PAID", "EVN_HCM", 1L),
                new Bill(2L, "WATER", 200_000, LocalDate.of(2024, 9, 15), "NOT_PAID", "SAVACO HCMC", 1L),
                new Bill(3L, "INTERNET", 300_000, LocalDate.of(2024, 9, 30), "NOT_PAID", "EVN_HCM", 1L),
                new Bill(4L, "ELECTRIC", 100_000, LocalDate.of(2024, 9, 2), "NOT_PAID", "EVN_HCM", 2L),
                new Bill(5L, "WATER", 200_000, LocalDate.of(2024, 9, 15), "NOT_PAID", "SAVACO HCMC", 2L),
                new Bill(6L, "INTERNET", 300_000, LocalDate.of(2024, 9, 30), "NOT_PAID", "EVN_HCM", 2L)
        ));

        mockBillsPool = new HashSet<>(Arrays.asList(
                new Bill(7L, "ELECTRIC", 100_000, LocalDate.of(2024, 9, 2), "NOT_PAID", "EVN_HCM", null),
                new Bill(8L, "WATER", 200_000, LocalDate.of(2024, 9, 15), "NOT_PAID", "SAVACO HCMC", null),
                new Bill(9L, "INTERNET", 300_000, LocalDate.of(2024, 9, 30), "NOT_PAID", "EVN_HCM", null),
                new Bill(10L, "ELECTRIC", 100_000, LocalDate.of(2024, 9, 2), "NOT_PAID", "EVN_HCM", null),
                new Bill(11L, "WATER", 200_000, LocalDate.of(2024, 9, 15), "NOT_PAID", "SAVACO HCMC", null),
                new Bill(12L, "INTERNET", 300_000, LocalDate.of(2024, 9, 30), "NOT_PAID", "EVN_HCM", null)
        ));
    }

    @Test
    public void testViewAllBills_whenHavingBills_thenProcessToOtherLayer() {
        // Arrange
        when(userInput.getCurrentUser()).thenReturn(user);
        when(billInput.findAllByUserId(anyLong())).thenReturn(mockBills);

        // Act
        billRequestAdapter.viewAllBills();

        // Assert
        verify(userInput, times(1)).getCurrentUser();
        verify(billInput, times(1)).findAllByUserId(user.getId());
    }

    @Test
    public void testViewAllBills_whenNoBills_thenProcessToOtherLayer() {
        // Arrange
        when(userInput.getCurrentUser()).thenReturn(user);

        // Act
        billRequestAdapter.viewAllBills();

        // Assert
        verify(userInput, times(1)).getCurrentUser();
        verify(billInput, times(1)).findAllByUserId(user.getId());
    }

    @Test
    public void testAddBills_whenTypeIntegerInput_thenProcessToOtherLayer() {
        // Arrange
        provideInput("7 8 9 \n");
        when(billInput.getBillsPool()).thenReturn(mockBillsPool);

        // Act
        billRequestAdapter.addBill();
        provideOutput();

        // Assert
        verify(billInput, times(1)).getBillsPool();
        verify(billInput, times(1)).addBills(any());
    }

    @Test
    public void testAddBills_whenTypeStringInput_thenDoNotProcessToOtherLayer() {
        // Arrange
        provideInput("abc \n");
        when(billInput.getBillsPool()).thenReturn(mockBillsPool);

        // Act, Assert
        InvalidInformation thrown = assertThrows(InvalidInformation.class, () -> billRequestAdapter.addBill());
        assertTrue(thrown.getMessage().contains("Invalid ids:"));

        verify(billInput, times(1)).getBillsPool();
        verify(billInput, never()).addBills(any());
    }

    @Test
    public void testAddBills_whenTypeStringAndIntegerInput_thenDoNotProcessToOtherLayer() {
        // Arrange
        provideInput("1 abc 2\n");
        when(billInput.getBillsPool()).thenReturn(mockBillsPool);

        // Act, Assert
        InvalidInformation thrown = assertThrows(InvalidInformation.class, () -> billRequestAdapter.addBill());
        assertTrue(thrown.getMessage().contains("Invalid ids:"));

        verify(billInput, times(1)).getBillsPool();
        verify(billInput, never()).addBills(any());
    }

    @Test
    public void testUpdateBills_whenTypeIntegerInput_thenProcessToOtherLayer() {
        // Arrange
        provideInput("7 8 9 \n");

        // Act
        billRequestAdapter.updateBills();
        provideOutput();

        // Assert
        verify(billInput, times(1)).payBill(any());
        verify(paymentHistoryInput, times(1)).saveHistory(any());
    }

    @Test
    public void testUpdateBills_whenTypeStringInput_thenDoNotProcessToOtherLayer() {
        // Arrange
        provideInput("abc \n");

        // Act, Assert
        InvalidInformation thrown = assertThrows(InvalidInformation.class, () -> billRequestAdapter.updateBills());
        assertTrue(thrown.getMessage().contains("Invalid ids:"));

        verify(billInput, never()).payBill(any());
        verify(paymentHistoryInput, never()).saveHistory(any());
    }

    @Test
    public void testUpdateBills_whenTypeStringAndIntegerInput_thenDoNotProcessToOtherLayer() {
        // Arrange
        provideInput("1 abc 2\n");
        when(billInput.getBillsPool()).thenReturn(mockBillsPool);

        // Act, Assert
        InvalidInformation thrown = assertThrows(InvalidInformation.class, () -> billRequestAdapter.addBill());
        assertTrue(thrown.getMessage().contains("Invalid ids:"));


        verify(billInput, never()).payBill(any());
        verify(paymentHistoryInput, never()).saveHistory(any());
    }

    @Test
    public void testDeleteBills_whenTypeIntegerInput_thenProcessToOtherLayer() {
        // Arrange
        provideInput("7 8 9 \n");

        // Act
        billRequestAdapter.deleteBills();
        provideOutput();

        // Assert
        verify(billInput, times(1)).deleteBills(any());
    }

    @Test
    public void testDeleteBills_whenTypeStringInput_thenDoNotProcessToOtherLayer() {
        // Arrange
        provideInput("abc \n");

        // Act, Assert
        InvalidInformation thrown = assertThrows(InvalidInformation.class, () -> billRequestAdapter.deleteBills());
        assertTrue(thrown.getMessage().contains("Invalid ids:"));

        verify(billInput, never()).payBill(any());
    }

    @Test
    public void testDeleteBills_whenTypeStringAndIntegerInput_thenDoNotProcessToOtherLayer() {
        // Arrange
        provideInput("1 abc 2\n");

        // Act, Assert
        InvalidInformation thrown = assertThrows(InvalidInformation.class, () -> billRequestAdapter.addBill());
        assertTrue(thrown.getMessage().contains("Invalid ids:"));


        verify(billInput, never()).deleteBills(any());
    }



}
