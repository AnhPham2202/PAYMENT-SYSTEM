package anh.pham.ports.outbound;

import anh.pham.domain.model.PaymentHistory;

import java.util.List;

public interface PaymentHistoryOutboundPorts {
    void saveHistory(List<PaymentHistory> payments);
    List<PaymentHistory> findAll() ;
}
