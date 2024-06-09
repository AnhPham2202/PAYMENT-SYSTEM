package anh.pham.adapters.outbound.persistence;

import anh.pham.adapters.outbound.repository.PaymentHistoryRepository;
import anh.pham.domain.model.PaymentHistory;
import anh.pham.exception.InvalidInformation;
import anh.pham.ports.outbound.PaymentHistoryOutboundPorts;

import java.util.List;
import java.util.Objects;

public class PaymentHistoryPersistenceAdapter implements PaymentHistoryOutboundPorts {
    private final PaymentHistoryRepository paymentHistoryRepository;

    public PaymentHistoryPersistenceAdapter(PaymentHistoryRepository paymentHistoryRepository) {
        this.paymentHistoryRepository = paymentHistoryRepository;
    }

    @Override
    public void saveHistory(List<PaymentHistory> payments) {
        if (payments.stream().anyMatch(p -> Objects.isNull(p.getBillId()))) throw new InvalidInformation("Invalid bill ids !!!");
        paymentHistoryRepository.save(payments);
    }

    @Override
    public List<PaymentHistory> findAll() {
        return paymentHistoryRepository.findAll();
    }
}
