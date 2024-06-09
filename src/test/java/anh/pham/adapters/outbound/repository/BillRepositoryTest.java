package anh.pham.adapters.outbound.repository;

import anh.pham.domain.model.Bill;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class BillRepositoryTest {
    BillRepository billRepository;
    @BeforeEach
    public void setup() {
        billRepository = new BillRepository();

    }

    @Test
    public void testFindByIds_whenAllValidId_thenReturnAllBillsThatMatch() {
        // Arrange
        Set<Long> ids = new HashSet<>();
        ids.add(1L);
        ids.add(2L);

        // Act
        Set<Bill> bills = billRepository.findByIds(ids);

        // Assert
        assertEquals(ids.size(), bills.size());
        assertTrue(bills.stream().allMatch(b -> ids.contains(b.getId())));
    }

    @Test
    public void testFindByIds_whenHavingSomeValidId_thenReturnAllBillsThatMatch() {
        // Arrange
        Set<Long> ids = new HashSet<>();
        ids.add(1L);
        ids.add(2L);
        ids.add(33L);

        // Act
        Set<Bill> bills = billRepository.findByIds(ids);

        // Assert
        assertEquals(2, bills.size());
        assertTrue(bills.stream().allMatch(b -> ids.contains(b.getId())));
    }

    @Test
    public void testFindByIds_whenAllValidIdCornerCase_thenReturnAllBillsThatMatch() {
        // Arrange
        Set<Long> ids = new HashSet<>();
        ids.add(1L);
        ids.add(2L);
        ids.add(3L);

        // Act
        Set<Bill> bills = billRepository.findByIds(ids);

        // Assert
        assertEquals(ids.size(), bills.size());
        assertTrue(bills.stream().allMatch(b -> ids.contains(b.getId())));
    }

    @Test
    public void testFindByIds_whenAllInvalidId_thenReturnSetSize0() {
        // Arrange
        Set<Long> ids = new HashSet<>();
        ids.add(11L);
        ids.add(22L);
        ids.add(33L);

        // Act
        Set<Bill> bills = billRepository.findByIds(ids);

        // Assert
        assertEquals(0, bills.size());
        assertTrue(bills.stream().noneMatch(b -> ids.contains(b.getId())));
    }

    @Test
    public void testFindAllByUserId_whenUserIdValid_thenReturnCorrectBills() {
        // Arrange
        Long userId = 1L;

        // Act
        Set<Bill> bills = billRepository.findAllByUserId(userId);

        // Assert
        assertTrue(bills.stream().allMatch(b -> userId.equals(b.getUserId())));
    }

    @Test
    public void testFindAllByUserId_whenUserIdInvalid_thenReturnCorrectBills() {
        // Arrange
        Long userId = 11L;

        // Act
        Set<Bill> bills = billRepository.findAllByUserId(userId);

        // Assert
        assertEquals(0, bills.size());
        assertTrue(bills.stream().noneMatch(b -> userId.equals(b.getUserId())));
    }


    @Test
    public void testGetBillsPool_whenBillsPoolHavingBills_thenReturnAllBills() {
        // Arrange
        // Act
        Set<Bill> bills = billRepository.getBillsPool();

        // Assert
        assertEquals(3, bills.size());
    }
    @Test
    public void testGetBillsPoolByIds_whenAllValidId_thenReturnAllBillsThatMatch() {
        // Arrange
        Set<Long> ids = new HashSet<>();
        ids.add(4L);
        ids.add(5L);

        // Act
        Set<Bill> bills = billRepository.getBillsPoolByIds(ids);

        // Assert
        assertEquals(ids.size(), bills.size());
        assertTrue(bills.stream().allMatch(b -> ids.contains(b.getId())));
    }

    @Test
    public void testGetBillsPoolByIds_whenHavingSomeValidId_thenReturnAllBillsThatMatch() {
        // Arrange
        Set<Long> ids = new HashSet<>();
        ids.add(1L);
        ids.add(4L);
        ids.add(5L);

        // Act
        Set<Bill> bills = billRepository.getBillsPoolByIds(ids);

        // Assert
        assertEquals(2, bills.size());
        assertTrue(bills.stream().allMatch(b -> ids.contains(b.getId())));
    }

    @Test
    public void testGetBillsPoolByIds_whenAllValidIdCornerCase_thenReturnAllBillsThatMatch() {
        // Arrange
        Set<Long> ids = new HashSet<>();
        ids.add(4L);
        ids.add(5L);
        ids.add(6L);

        // Act
        Set<Bill> bills = billRepository.getBillsPoolByIds(ids);

        // Assert
        assertEquals(ids.size(), bills.size());
        assertTrue(bills.stream().allMatch(b -> ids.contains(b.getId())));
    }

    @Test
    public void testGetBillsPoolByIds_whenAllInvalidId_thenReturnSetSize0() {
        // Arrange
        Set<Long> ids = new HashSet<>();
        ids.add(11L);
        ids.add(22L);
        ids.add(33L);

        // Act
        Set<Bill> bills = billRepository.getBillsPoolByIds(ids);

        // Assert
        assertEquals(0, bills.size());
        assertTrue(bills.stream().noneMatch(b -> ids.contains(b.getId())));
    }

}
