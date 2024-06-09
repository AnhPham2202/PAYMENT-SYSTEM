package anh.pham.domain.service;

import anh.pham.domain.model.Bill;
import anh.pham.domain.model.PaymentHistory;
import anh.pham.ports.inbound.PaymentHistoryInboundPorts;
import anh.pham.ports.outbound.PaymentHistoryOutboundPorts;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class PaymentHistoryService implements PaymentHistoryInboundPorts {
    private final PaymentHistoryOutboundPorts paymentHistoryOutboundPorts;

    public PaymentHistoryService(PaymentHistoryOutboundPorts paymentHistoryOutboundPorts) {
        this.paymentHistoryOutboundPorts = paymentHistoryOutboundPorts;
    }


    @Override
    public void saveHistory(Set<Bill> bills) {
        List<PaymentHistory> histories = new ArrayList<>();
        bills.forEach(payment -> histories.add(new PaymentHistory(
                payment.getType(),
                payment.getAmount(),
                LocalDate.now(),
                "PROCESSED",
                payment.getProvider(),
                payment.getId()
        )));
        paymentHistoryOutboundPorts.saveHistory(histories);
    }

    @Override
    public List<PaymentHistory> findAll() {
        return paymentHistoryOutboundPorts.findAll();
    }
}
