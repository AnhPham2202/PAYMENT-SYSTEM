package anh.pham.adapters.outbound.repository;

import anh.pham.domain.model.PaymentHistory;

import java.util.ArrayList;
import java.util.List;

public class PaymentHistoryRepository {
    private static final List<PaymentHistory> HISTORY = new ArrayList<>();

    public void save(List<PaymentHistory> histories) {
        HISTORY.addAll(histories);
    }

    public List<PaymentHistory> findAll() {
        return HISTORY;
    }

}
