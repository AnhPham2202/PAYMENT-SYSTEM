package anh.pham.ports.inbound;

import anh.pham.domain.model.Bill;
import anh.pham.domain.model.PaymentHistory;

import java.util.List;
import java.util.Set;

public interface PaymentHistoryInboundPorts {
    void saveHistory(Set<Bill> bills);
    List<PaymentHistory> findAll() ;

}
