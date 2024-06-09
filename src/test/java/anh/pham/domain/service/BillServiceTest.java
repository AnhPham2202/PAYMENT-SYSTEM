package anh.pham.domain.service;

import anh.pham.domain.model.Bill;
import anh.pham.ports.outbound.BillOutboundPorts;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BillServiceTest {
    Set<Bill> mockBills;
    Set<Bill> mockBillsPool;

    @InjectMocks
    BillService billService;
    @Mock
    BillOutboundPorts billOutput;

    @BeforeEach
    public void setup() {
        mockBills = new HashSet<>(Arrays.asList(
                new Bill(1L, "ELECTRIC", 100000, LocalDate.of(2024, 9, 2), "NOT_PAID", "EVN_HCM", 1L),
                new Bill(2L, "WATER", 200000, LocalDate.of(2024, 9, 15), "NOT_PAID", "SAVACO HCMC", 1L),
                new Bill(3L, "INTERNET", 300000, LocalDate.of(2024, 9, 30), "NOT_PAID", "EVN_HCM", 1L),
                new Bill(4L, "ELECTRIC", 100000, LocalDate.of(2024, 9, 2), "NOT_PAID", "EVN_HCM", 2L),
                new Bill(5L, "WATER", 200000, LocalDate.of(2024, 9, 15), "NOT_PAID", "SAVACO HCMC", 2L),
                new Bill(6L, "INTERNET", 300000, LocalDate.of(2024, 9, 30), "NOT_PAID", "EVN_HCM", 2L)
        ));

        mockBillsPool = new HashSet<>(Arrays.asList(
                new Bill(7L, "ELECTRIC", 100000, LocalDate.of(2024, 9, 2), "NOT_PAID", "EVN_HCM", null),
                new Bill(8L, "WATER", 200000, LocalDate.of(2024, 9, 15), "NOT_PAID", "SAVACO HCMC", null),
                new Bill(9L, "INTERNET", 300000, LocalDate.of(2024, 9, 30), "NOT_PAID", "EVN_HCM", null),
                new Bill(10L, "ELECTRIC", 100000, LocalDate.of(2024, 9, 2), "NOT_PAID", "EVN_HCM", null),
                new Bill(11L, "WATER", 200000, LocalDate.of(2024, 9, 15), "NOT_PAID", "SAVACO HCMC", null),
                new Bill(12L, "INTERNET", 300000, LocalDate.of(2024, 9, 30), "NOT_PAID", "EVN_HCM", null)
        ));
    }

    @Test
    public void testFindAllByUserId_whenUserIdFound_thenReturnBills() {
        // Arrange
        Long userId = 1L;
        Set<Bill> billResult = mockBills.stream().filter(b -> userId.equals(b.getUserId())).collect(Collectors.toSet());
        when(billOutput.findAllByUserId(userId)).thenReturn(billResult);

        // Act
        Set<Bill> bills = billService.findAllByUserId(userId);

        // Assert
        verify(billOutput, times(1)).findAllByUserId(userId);
        assertEquals(billResult.size(), bills.size());
        assertTrue(bills.stream().allMatch(b -> userId.equals(b.getUserId())));
    }

    @Test
    public void testFindAllByUserId_whenUserIdNotFound_thenReturn0Bill() {
        // Arrange
        Long userId = 3L;
        Set<Bill> billResult = mockBills.stream().filter(b -> userId.equals(b.getUserId())).collect(Collectors.toSet());
        when(billOutput.findAllByUserId(userId)).thenReturn(billResult);

        // Act
        Set<Bill> bills = billService.findAllByUserId(userId);

        // Assert
        verify(billOutput, times(1)).findAllByUserId(userId);
        assertEquals(0, bills.size());
        assertTrue(bills.stream().noneMatch(b -> userId.equals(b.getUserId())));
    }


    @Test
    public void testPayBills_whenAllValidBills_thenPayBills() {
        // Arrange
        Set<Long> ids = new HashSet<>();
        ids.add(1L);
        ids.add(2L);

        Set<Bill> billResult = mockBills.stream().filter(b -> ids.contains(b.getId())).collect(Collectors.toSet());
        when(billOutput.updateBills(ids)).thenReturn(billResult);

        // Act
        Set<Bill> bills = billService.payBill(ids);

        // Assert
        verify(billOutput, times(1)).updateBills(ids);
        assertEquals(billResult.size(), bills.size());
        assertTrue(bills.stream().allMatch(b -> ids.contains(b.getId())));
    }

    @Test
    public void testPayBills_whenHavingSomeInvalidBills_thenStillPay() {
        // Arrange
        Set<Long> ids = new HashSet<>();
        ids.add(1L);
        ids.add(10L);

        Set<Bill> billResult = mockBills.stream().filter(b -> ids.contains(b.getId())).collect(Collectors.toSet());
        when(billOutput.updateBills(ids)).thenReturn(billResult);

        // Act
        Set<Bill> bills = billService.payBill(ids);

        // Assert
        verify(billOutput, times(1)).updateBills(ids);
        assertEquals(billResult.size(), bills.size());
        assertDoesNotThrow(() -> billService.payBill(ids));
        assertTrue(bills.stream().allMatch(b -> ids.contains(b.getId())));
    }

    @Test
    public void testGetBillsPool_whenPoolHavingBills_thenReturnAllBillInPool() {
        // Arrange
        when(billOutput.getBillsPool()).thenReturn(mockBillsPool);

        // Act
        Set<Bill> bills = billService.getBillsPool();

        // Assert
        verify(billOutput, times(1)).getBillsPool();
        assertEquals(mockBillsPool.size(), bills.size());
        assertSame(bills, mockBillsPool);
    }

    @Test
    public void testGetBillsPool_whenBillsPoolEmpty_thenReturnSize0() {
        // Arrange
        mockBillsPool = new HashSet<>();
        when(billOutput.getBillsPool()).thenReturn(mockBillsPool);

        // Act
        Set<Bill> bills = billService.getBillsPool();

        // Assert
        verify(billOutput, times(1)).getBillsPool();
        assertDoesNotThrow(() -> billService.getBillsPool());
        assertEquals(0, bills.size());
    }

    @Test
    public void testAddPayments_whenAllValidBillIds_thenReturnThoseBills() {
        // Arrange
        Set<Long> ids = new HashSet<>();
        ids.add(7L);
        ids.add(8L);

        Set<Bill> billResult = mockBillsPool.stream().filter(b -> ids.contains(b.getId())).collect(Collectors.toSet());
        when(billOutput.addBills(ids)).thenReturn(billResult);

        // Act
        Set<Bill> bills = billService.addBills(ids);

        // Assert
        verify(billOutput, times(1)).addBills(ids);
        assertEquals(billResult.size(), bills.size());
        assertSame(bills, billResult);
    }

    @Test
    public void testAddPayments_whenHavingSomeInvalidBillIds_thenReturnOnlyValidBills() {
        // Arrange
        Set<Long> ids = new HashSet<>();
        ids.add(1L);
        ids.add(8L);

        Set<Bill> billResult = mockBillsPool.stream().filter(b -> ids.contains(b.getId())).collect(Collectors.toSet());
        when(billOutput.addBills(ids)).thenReturn(billResult);

        // Act
        Set<Bill> bills = billService.addBills(ids);

        // Assert
        verify(billOutput, times(1)).addBills(ids);
        assertEquals(billResult.size(), bills.size());
        assertSame(bills, billResult);
    }

    @Test
    public void testAddPayments_whenAllInvalidBillIds_thenReturn0Bills() {
        // Arrange
        Set<Long> ids = new HashSet<>();
        ids.add(1L);
        ids.add(2L);

        Set<Bill> billResult = mockBillsPool.stream().filter(b -> ids.contains(b.getId())).collect(Collectors.toSet());
        when(billOutput.addBills(ids)).thenReturn(billResult);

        // Act
        Set<Bill> bills = billService.addBills(ids);

        // Assert
        verify(billOutput, times(1)).addBills(ids);
        assertEquals(0, bills.size());
    }

    @Test
    public void testDeletePayments_whenAllValidBillIds_thenReturnThoseBills() {
        // Arrange
        Set<Long> ids = new HashSet<>();
        ids.add(1L);
        ids.add(2L);

        Set<Bill> billResult = mockBills.stream().filter(b -> ids.contains(b.getId())).collect(Collectors.toSet());
        when(billOutput.deleteBills(ids)).thenReturn(billResult);

        // Act
        Set<Bill> bills = billService.deleteBills(ids);

        // Assert
        verify(billOutput, times(1)).deleteBills(ids);
        assertEquals(billResult.size(), bills.size());
        assertSame(bills, billResult);
    }

    @Test
    public void testDeletePayments_whenHavingSomeInvalidBillIds_thenReturnOnlyValidBills() {
        // Arrange
        Set<Long> ids = new HashSet<>();
        ids.add(1L);
        ids.add(8L);

        Set<Bill> billResult = mockBills.stream().filter(b -> ids.contains(b.getId())).collect(Collectors.toSet());
        when(billOutput.deleteBills(ids)).thenReturn(billResult);

        // Act
        Set<Bill> bills = billService.deleteBills(ids);

        // Assert
        verify(billOutput, times(1)).deleteBills(ids);
        assertEquals(billResult.size(), bills.size());
        assertSame(bills, billResult);
    }

    @Test
    public void testDeletePayments_whenAllInvalidBillIds_thenReturn0Bills() {
        // Arrange
        Set<Long> ids = new HashSet<>();
        ids.add(7L);
        ids.add(8L);

        Set<Bill> billResult = mockBills.stream().filter(b -> ids.contains(b.getId())).collect(Collectors.toSet());
        when(billOutput.deleteBills(ids)).thenReturn(billResult);

        // Act
        Set<Bill> bills = billService.deleteBills(ids);

        // Assert
        verify(billOutput, times(1)).deleteBills(ids);
        assertEquals(0, bills.size());
    }
}
