package anh.pham.adapters.outbound.repository;


import anh.pham.domain.model.Bill;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class BillRepository {
    private static final Set<Bill> BILLS = new HashSet<>(Arrays.asList(
            new Bill(1L, "ELECTRIC", 100_000, LocalDate.of(2024, 9, 2), "NOT_PAID", "EVN_HCM", 1L),
            new Bill(2L, "WATER", 200_000, LocalDate.of(2024, 9, 15), "NOT_PAID", "SAVACO HCMC", 1L),
            new Bill(3L, "INTERNET", 300_000, LocalDate.of(2024, 9, 30), "NOT_PAID", "EVN_HCM", 1L)
    ));

    private static final Set<Bill> BILLS_POOL = new HashSet<>(Arrays.asList(
            new Bill(4L, "ELECTRIC", 100_000, LocalDate.of(2024, 9, 2), "NOT_PAID", "EVN_HN", null),
            new Bill(5L, "WATER", 200_000, LocalDate.of(2024, 9, 15), "NOT_PAID", "SAVACO HN", null),
            new Bill(6L, "INTERNET", 300_000, LocalDate.of(2024, 9, 30), "NOT_PAID", "EVN_HN", null)
    ));

    public Set<Bill> findByIds(Set<Long> ids) {
        return BILLS.stream().filter(u -> ids.contains(u.getId())).collect(Collectors.toSet());
    }

    public Set<Bill> findAllByUserId(Long userId) {
        return BILLS.stream().filter(u -> u.getUserId().equals(userId)).collect(Collectors.toSet());
    }

    public Set<Bill> getBillsPool() {
        return BILLS_POOL;
    }

    public void deleteBillsPool(Set<Bill> bills) {
        BILLS_POOL.removeAll(bills);
    }

    public void addBills(Set<Bill> bills) {
        BILLS.addAll(bills);
    }

    public void delete(Set<Bill> bills) {
        BILLS.removeAll(bills);
    }

    public void addBillsPool(Set<Bill> bills) {
        BILLS_POOL.addAll(bills);
    }

    public Set<Bill> getBillsPoolByIds(Set<Long> ids) {
        return BILLS_POOL.stream().filter(p -> ids.contains(p.getId())).collect(Collectors.toSet());
    }


}
