package anh.pham.adapters.inbound.request;

import anh.pham.domain.model.PaymentHistory;
import anh.pham.ports.inbound.PaymentHistoryInboundPorts;

import java.util.List;

public class PaymentHistoryRequestAdapter {
    private final PaymentHistoryInboundPorts paymentInput;

    public PaymentHistoryRequestAdapter(PaymentHistoryInboundPorts paymentInput) {
        this.paymentInput = paymentInput;
    }

    public void viewAllPayments() {
        List<PaymentHistory> payments = paymentInput.findAll();
        printResult(payments);
    }

    private void printResult(List<PaymentHistory> payments) {
        System.out.printf("%-22s%-22s%-22s%-22s%-22s%-22s%-22s\n", "NO.", "TYPE", "AMOUNT", "DUE DATE", "STATE", "PROVIDER", "BILL ID");
        System.out.println("===========================================================================================================================================");

        for (int i = 0; i < payments.size(); i++) {
            System.out.printf("%-22s%-22s%-22o%-22s%-22s%-22s%-22s\n", i+1, payments.get(i).getType(), payments.get(i).getAmount(), payments.get(i).getProcessDate(), payments.get(i).getState(), payments.get(i).getProvider(), payments.get(i).getBillId());
        }
        System.out.println("===========================================================================================================================================");
    }
}
