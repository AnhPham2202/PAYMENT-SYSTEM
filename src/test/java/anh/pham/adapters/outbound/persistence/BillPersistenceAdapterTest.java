package anh.pham.adapters.outbound.persistence;

import anh.pham.adapters.outbound.repository.BillRepository;
import anh.pham.domain.model.Bill;
import anh.pham.domain.model.User;
import anh.pham.exception.BadRequestException;
import anh.pham.exception.InvalidInformation;
import anh.pham.ports.inbound.UserInboundPorts;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BillPersistenceAdapterTest {
    User user;
    Set<Bill> mockBills;
    Set<Bill> mockBillsPool;
    @Mock
    BillRepository billRepository;
    @Mock
    UserInboundPorts userInput;
    @InjectMocks
    BillPersistenceAdapter billPersistenceAdapter;

    @BeforeEach
    public void setup() {
        user = new User(2L, "anh pham", 1_000_000);
        mockBills = new HashSet<>(Arrays.asList(
                new Bill(1L, "ELECTRIC", 100_000, LocalDate.of(2024, 9, 2), "NOT_PAID", "EVN_HCM", 1L),
                new Bill(2L, "WATER", 200_000, LocalDate.of(2024, 9, 15), "NOT_PAID", "SAVACO HCMC", 1L),
                new Bill(3L, "INTERNET", 300_000, LocalDate.of(2024, 9, 30), "NOT_PAID", "EVN_HCM", 1L),
                new Bill(4L, "ELECTRIC", 100_000, LocalDate.of(2024, 9, 2), "NOT_PAID", "EVN_HCM", 2L),
                new Bill(5L, "WATER", 200_000, LocalDate.of(2024, 9, 15), "NOT_PAID", "SAVACO HCMC", 2L),
                new Bill(6L, "INTERNET", 300_000, LocalDate.of(2024, 9, 30), "NOT_PAID", "EVN_HCM", 2L)
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
    public void testFindAllByUserId_whenHavingBills_thenReturnBillOfThatUser() {
        // Arrange
        Long userId = 1L;
        Set<Bill> billResult = mockBills.stream().filter(b -> userId.equals(b.getUserId())).collect(Collectors.toSet());
        when(billRepository.findAllByUserId(userId)).thenReturn(billResult);

        // Act
        Set<Bill> bills = billPersistenceAdapter.findAllByUserId(userId);

        // Assert
        verify(billRepository, times(1)).findAllByUserId(userId);
        assertEquals(billResult.size(), bills.size());
        assertTrue(bills.stream().allMatch(b -> userId.equals(b.getUserId())));
    }

    @Test
    public void testFindAllByUserId_whenNoBills_thenReturn0Bill() {
        // Arrange
        Long userId = 3L;
        Set<Bill> billResult = mockBills.stream().filter(b -> userId.equals(b.getUserId())).collect(Collectors.toSet());
        when(billRepository.findAllByUserId(userId)).thenReturn(billResult);

        // Act
        Set<Bill> bills = billPersistenceAdapter.findAllByUserId(userId);

        // Assert
        verify(billRepository, times(1)).findAllByUserId(userId);
        assertEquals(0, bills.size());
        assertTrue(bills.stream().noneMatch(b -> userId.equals(b.getUserId())));
    }

    @Test
    public void testUpdateBills_whenAllValidBillsAndHavingEnoughBalance_thenPayAllThatBills() {
        // Arrange
        Set<Long> billIds = new HashSet<>();
        billIds.add(1L);
        billIds.add(2L);

        Set<Bill> billResult = mockBills.stream().filter(b -> billIds.contains(b.getId())).collect(Collectors.toSet());
        when(billRepository.findByIds(billIds)).thenReturn(billResult);
        when(userInput.getBalance()).thenReturn(user.getBalance());

        int expectedTotal = billResult.stream().mapToInt(Bill::getAmount).sum();

        // Act
        Set<Bill> bills = billPersistenceAdapter.updateBills(billIds);
        int actualTotal = billResult.stream().mapToInt(Bill::getAmount).sum();

        // Assert
        verify(billRepository, times(1)).findByIds(billIds);
        verify(userInput, times(1)).updateBalance(any());

        assertEquals(billIds.size(), bills.size());
        assertEquals(user.getBalance() - expectedTotal, user.getBalance() - actualTotal);
        assertTrue(bills.stream().allMatch(b -> b.getState().equals("PAID")));
    }

    @Test
    public void testUpdateBills_whenAllValidBillsAndHavingEnoughBalanceCornerCase_thenPayAllThatBills() {
        // Arrange

        Set<Long> billIds = new HashSet<>();
        billIds.add(1L);
        billIds.add(2L);

        Set<Bill> billResult = mockBills.stream().filter(b -> billIds.contains(b.getId())).collect(Collectors.toSet());
        when(billRepository.findByIds(billIds)).thenReturn(billResult);
        when(userInput.getBalance()).thenReturn(user.getBalance());

        int expectedTotal = billResult.stream().mapToInt(Bill::getAmount).sum();
        user.setBalance(expectedTotal);
        // Act
        Set<Bill> bills = billPersistenceAdapter.updateBills(billIds);
        int actualTotal = billResult.stream().mapToInt(Bill::getAmount).sum();

        // Assert
        verify(billRepository, times(1)).findByIds(billIds);
        verify(userInput, times(1)).updateBalance(any());

        assertEquals(billIds.size(), bills.size());
        assertEquals(user.getBalance() - expectedTotal, user.getBalance() - actualTotal);
        assertEquals(0, user.getBalance() - actualTotal);
        assertTrue(bills.stream().allMatch(b -> b.getState().equals("PAID")));
    }

    @Test
    public void testUpdateBills_whenAllValidBillsAndOnlyEnoughBalanceFor1_thenDoNotPayAny() {
        // Arrange
        int balance = 200_000;
        user.setBalance(balance);
        Set<Long> billIds = new HashSet<>();
        billIds.add(1L);
        billIds.add(2L);

        Set<Bill> billResult = mockBills.stream().filter(b -> billIds.contains(b.getId())).collect(Collectors.toSet());
        when(billRepository.findByIds(billIds)).thenReturn(billResult);
        when(userInput.getBalance()).thenReturn(user.getBalance());

        // Act
        BadRequestException thrown = assertThrows(
                BadRequestException.class,
                () -> billPersistenceAdapter.updateBills(billIds)
        );

        // Assert
        verify(billRepository, times(1)).findByIds(billIds);
        verify(userInput, never()).updateBalance(any());

        assertEquals("You balance not enough money to pay these bills !!!", thrown.getMessage());
    }

    @Test
    public void testUpdateBills_whenAllValidBillsAndOnlyEnoughBalanceForAll_thenDoNotPayAny() {
        // Arrange
        int balance = 1;
        user.setBalance(balance);
        Set<Long> billIds = new HashSet<>();
        billIds.add(1L);
        billIds.add(2L);

        Set<Bill> billResult = mockBills.stream().filter(b -> billIds.contains(b.getId())).collect(Collectors.toSet());
        when(billRepository.findByIds(billIds)).thenReturn(billResult);
        when(userInput.getBalance()).thenReturn(user.getBalance());

        // Act
        BadRequestException thrown = assertThrows(
                BadRequestException.class,
                () -> billPersistenceAdapter.updateBills(billIds)
        );

        // Assert
        verify(billRepository, times(1)).findByIds(billIds);
        verify(userInput, never()).updateBalance(any());

        assertEquals("You balance not enough money to pay these bills !!!", thrown.getMessage());
    }

    @Test
    public void testUpdateBills_whenSomeInvalidBillsAndEnoughPayForOthersValid_thenDoNotPayAny() {
        // Arrange
        Set<Long> billIds = new HashSet<>();
        billIds.add(111L);
        billIds.add(2L);

        Set<Bill> billResult = mockBills.stream().filter(b -> billIds.contains(b.getId())).collect(Collectors.toSet());
        when(billRepository.findByIds(billIds)).thenReturn(billResult);

        // Act
        InvalidInformation thrown = assertThrows(
                InvalidInformation.class,
                () -> billPersistenceAdapter.updateBills(billIds)
        );

        // Assert
        verify(billRepository, times(1)).findByIds(billIds);
        verify(userInput, never()).updateBalance(any());
        verify(userInput, never()).getBalance();

        assertEquals("Invalid payment id !!!", thrown.getMessage());
    }

    @Test
    public void testUpdateBills_whenAllInvalidBills_thenDoNotPayAny() {
        // Arrange
        Set<Long> billIds = new HashSet<>();
        billIds.add(111L);
        billIds.add(222L);

        Set<Bill> billResult = mockBills.stream().filter(b -> billIds.contains(b.getId())).collect(Collectors.toSet());
        when(billRepository.findByIds(billIds)).thenReturn(billResult);

        // Act
        InvalidInformation thrown = assertThrows(
                InvalidInformation.class,
                () -> billPersistenceAdapter.updateBills(billIds)
        );

        // Assert
        verify(billRepository, times(1)).findByIds(billIds);
        verify(userInput, never()).updateBalance(any());
        verify(userInput, never()).getBalance();

        assertEquals("Invalid payment id !!!", thrown.getMessage());
    }

    @Test
    public void testGetBillPools_whenHavingBillsInPool_thenReturnAll() {
        // Arrange
        when(billRepository.getBillsPool()).thenReturn(mockBillsPool);

        // Act
        Set<Bill> bills = billPersistenceAdapter.getBillsPool();

        // Assert
        verify(billRepository, times(1)).getBillsPool();

        assertEquals(mockBillsPool.size(), bills.size());
        assertSame(bills, mockBillsPool);
    }

    @Test
    public void testGetBillPools_whenHavingNoBillInPool_thenReturn0Bill() {
        // Arrange
        mockBillsPool = new HashSet<>();
        when(billRepository.getBillsPool()).thenReturn(mockBillsPool);

        // Act
        Set<Bill> bills = billPersistenceAdapter.getBillsPool();

        // Assert
        verify(billRepository, times(1)).getBillsPool();

        assertEquals(0, bills.size());
    }

    @Test
    public void testAddBills_whenAllValidBills_thenReturnThoseBills() {
        // Arrange
        Set<Long> billPoolIds = new HashSet<>();
        billPoolIds.add(7L);
        billPoolIds.add(8L);

        Set<Bill> billPoolResult = mockBillsPool.stream().filter(b -> billPoolIds.contains(b.getId())).collect(Collectors.toSet());
        when(userInput.getCurrentUser()).thenReturn(user);
        when(billRepository.getBillsPoolByIds(billPoolIds)).thenReturn(billPoolResult);

        // Act
        Set<Bill> bills = billPersistenceAdapter.addBills(billPoolIds);

        // Assert
        verify(billRepository, times(1)).deleteBillsPool(any());
        verify(billRepository, times(1)).addBills(billPoolResult);

        assertEquals(billPoolResult.size(), bills.size());
        assertSame(billPoolResult, bills);
        assertTrue(bills.stream().allMatch(b -> b.getUserId().equals(user.getId())));
    }

    @Test
    public void testAddBills_whenAllValidBillsCornerCase_thenReturnThoseBills() {
        // Arrange
        Set<Long> billPoolIds = new HashSet<>();
        billPoolIds.add(7L);
        billPoolIds.add(8L);
        billPoolIds.add(9L);
        billPoolIds.add(10L);
        billPoolIds.add(11L);
        billPoolIds.add(12L);

        Set<Bill> billPoolResult = mockBillsPool.stream().filter(b -> billPoolIds.contains(b.getId())).collect(Collectors.toSet());
        when(userInput.getCurrentUser()).thenReturn(user);
        when(billRepository.getBillsPoolByIds(billPoolIds)).thenReturn(billPoolResult);

        // Act
        Set<Bill> bills = billPersistenceAdapter.addBills(billPoolIds);

        // Assert
        verify(billRepository, times(1)).deleteBillsPool(any());
        verify(billRepository, times(1)).addBills(billPoolResult);

        assertEquals(billPoolResult.size(), bills.size());
        assertTrue(bills.stream().allMatch(b -> b.getUserId().equals(user.getId())));
    }

    @Test
    public void testAddBills_whenHavingSomeInvalidBills_thenThrowsException() {
        // Arrange
        Set<Long> billPoolIds = new HashSet<>();
        billPoolIds.add(1L);
        billPoolIds.add(8L);

        // Act
        InvalidInformation thrown = assertThrows(
                InvalidInformation.class,
                () -> billPersistenceAdapter.addBills(billPoolIds)
        );

        // Assert
        verify(billRepository, never()).deleteBillsPool(any());
        verify(billRepository, never()).addBills(any());

        assertEquals("Invalid bill ids !!!", thrown.getMessage());
    }

    @Test
    public void testAddBills_whenHavingAllInvalidBills_thenThrowsException() {
        // Arrange
        Set<Long> billPoolIds = new HashSet<>();
        billPoolIds.add(1L);
        billPoolIds.add(2L);

        // Act
        InvalidInformation thrown = assertThrows(
                InvalidInformation.class,
                () -> billPersistenceAdapter.addBills(billPoolIds)
        );

        // Assert
        verify(billRepository, never()).deleteBillsPool(any());
        verify(billRepository, never()).addBills(any());

        assertEquals("Invalid bill ids !!!", thrown.getMessage());
    }

    @Test
    public void testDeleteBills_whenAllValidBills_thenReturnThoseBills() {
        // Arrange
        Set<Long> billIds = new HashSet<>();
        billIds.add(1L);
        billIds.add(2L);

        Set<Bill> billResult = mockBills.stream().filter(b -> billIds.contains(b.getId())).collect(Collectors.toSet());
        when(billRepository.findByIds(billIds)).thenReturn(billResult);

        // Act
        Set<Bill> bills = billPersistenceAdapter.deleteBills(billIds);

        // Assert
        verify(billRepository, times(1)).delete(billResult);
        verify(billRepository, times(1)).addBillsPool(billResult);

        assertEquals(billResult.size(), bills.size());
        assertSame(billResult, bills);
        assertTrue(bills.stream().allMatch(b -> Objects.isNull(b.getUserId())));
    }

    @Test
    public void testDeleteBills_whenAllValidBillsCornerCase_thenReturnThoseBills() {
        // Arrange
        Set<Long> billIds = new HashSet<>();
        billIds.add(1L);
        billIds.add(2L);
        billIds.add(3L);
        billIds.add(4L);
        billIds.add(5L);
        billIds.add(6L);

        Set<Bill> billResult = mockBills.stream().filter(b -> billIds.contains(b.getId())).collect(Collectors.toSet());
        when(billRepository.findByIds(billIds)).thenReturn(billResult);

        // Act
        Set<Bill> bills = billPersistenceAdapter.deleteBills(billIds);

        // Assert
        verify(billRepository, times(1)).delete(billResult);
        verify(billRepository, times(1)).addBillsPool(billResult);

        assertEquals(billResult.size(), bills.size());
        assertSame(billResult, bills);
        assertTrue(bills.stream().allMatch(b -> Objects.isNull(b.getUserId())));
    }

    @Test
    public void testDeleteBills_whenHavingSomeInvalidBills_thenThrowsException() {
        // Arrange
        Set<Long> billIds = new HashSet<>();
        billIds.add(1L);
        billIds.add(8L);

        // Act
        InvalidInformation thrown = assertThrows(
                InvalidInformation.class,
                () -> billPersistenceAdapter.deleteBills(billIds)
        );

        // Assert
        verify(billRepository, never()).delete(any());
        verify(billRepository, never()).addBillsPool(any());

        assertEquals("Invalid bill ids !!!", thrown.getMessage());
    }

    @Test
    public void testDeleteBills_whenHavingAllInvalidBills_thenThrowsException() {
        // Arrange
        Set<Long> billIds = new HashSet<>();
        billIds.add(7L);
        billIds.add(8L);

        // Act
        InvalidInformation thrown = assertThrows(
                InvalidInformation.class,
                () -> billPersistenceAdapter.deleteBills(billIds)
        );

        // Assert
        verify(billRepository, never()).delete(any());
        verify(billRepository, never()).addBillsPool(any());

        assertEquals("Invalid bill ids !!!", thrown.getMessage());
    }

}
