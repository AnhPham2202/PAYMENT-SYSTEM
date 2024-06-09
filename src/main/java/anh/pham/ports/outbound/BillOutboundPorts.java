package anh.pham.ports.outbound;

import anh.pham.domain.model.Bill;

import java.util.Set;

public interface BillOutboundPorts {
    Set<Bill> findAllByUserId(Long userId);
    Set<Bill> updateBills(Set<Long> ids);
    Set<Bill> getBillsPool();
    Set<Bill> addBills(Set<Long> ids);
    Set<Bill> deleteBills(Set<Long> ids);
}
