package anh.pham.ports.inbound;

import anh.pham.domain.model.Bill;

import java.util.Set;

public interface BillInboundPorts {
    Set<Bill> findAllByUserId(Long userId);
    Set<Bill> payBill(Set<Long> id);
    Set<Bill> getBillsPool();
    Set<Bill> addBills(Set<Long> ids);
    Set<Bill> deleteBills(Set<Long> paymentIds);
}
