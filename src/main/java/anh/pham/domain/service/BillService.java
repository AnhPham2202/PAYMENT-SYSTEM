package anh.pham.domain.service;

import anh.pham.domain.model.Bill;
import anh.pham.ports.inbound.BillInboundPorts;
import anh.pham.ports.outbound.BillOutboundPorts;

import java.util.Set;

public class BillService implements BillInboundPorts {
    private final BillOutboundPorts billPersistence;

    public BillService(BillOutboundPorts billPersistence) {
        this.billPersistence = billPersistence;
    }

    @Override
    public Set<Bill> findAllByUserId(Long userId) {
        return billPersistence.findAllByUserId(userId);
    }

    @Override
    public Set<Bill> payBill(Set<Long> ids) {
        return billPersistence.updateBills(ids);
    }

    @Override
    public Set<Bill> getBillsPool() {
        return billPersistence.getBillsPool();
    }

    @Override
    public Set<Bill> addBills(Set<Long> ids) {
        return billPersistence.addBills(ids);
    }

    @Override
    public Set<Bill> deleteBills(Set<Long> ids) {
        return billPersistence.deleteBills(ids);
    }
}
