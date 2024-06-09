package anh.pham.adapters.inbound.request;

import anh.pham.exception.InvalidInformation;
import anh.pham.domain.model.Bill;
import anh.pham.ports.inbound.PaymentHistoryInboundPorts;
import anh.pham.ports.inbound.BillInboundPorts;
import anh.pham.ports.inbound.UserInboundPorts;

import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class BillRequestAdapter {
    private final BillInboundPorts billInput;
    private final PaymentHistoryInboundPorts paymentHistoryInput;
    private final UserInboundPorts userInput;

    public BillRequestAdapter(BillInboundPorts billInput, UserInboundPorts userInput, PaymentHistoryInboundPorts paymentHistoryInput) {
        this.billInput = billInput;
        this.userInput = userInput;
        this.paymentHistoryInput = paymentHistoryInput;
    }

    public void viewAllBills() {
        Set<Bill> bills = billInput.findAllByUserId(userInput.getCurrentUser().getId());
        printResult(bills);
    }

    public void addBill() {
        System.out.println("Here some available payments if you want to pay them :) ");
        Set<Bill> bills = billInput.getBillsPool();
        printResult(bills);
        System.out.println("Enter payment ids: ");
        Set<Long> billIds = getIdsFromInput();

        System.out.println("Successfully add these payments");
        printResult(billInput.addBills(billIds));
    }

    public void updateBills() {
        System.out.println("Enter payment ids: ");
        Set<Long> paymentIds = getIdsFromInput();

        Set<Bill> bills = billInput.payBill(paymentIds);
        paymentHistoryInput.saveHistory(bills);
        System.out.println("Your current available balance: " + userInput.getBalance());
    }

    public void deleteBills() {
        System.out.println("Enter payment ids: ");
        Set<Long> paymentIds = getIdsFromInput();

        System.out.println("Successfully delete these payments");
        printResult(billInput.deleteBills(paymentIds));
    }

    private Set<Long> getIdsFromInput() {
        Set<Long> paymentIds = new HashSet<>();
        Scanner scanner = new Scanner(System.in);
        String ids = scanner.nextLine();
        String[] idStrings = ids.split("\\s+");

        for (String token : idStrings) {
            try {
                Long id = Long.parseLong(token);
                paymentIds.add(id);
            } catch (NumberFormatException e) {
                throw new InvalidInformation("Invalid ids: " + token);
            }
        }
        return paymentIds;
    }

    private void printResult(Set<Bill> bills) {
        System.out.printf("%-22s%-22s%-22s%-22s%-22s%-22s\n", "ID", "TYPE", "AMOUNT", "DUE DATE", "STATE", "PROVIDER");
        System.out.println("============================================================================================================================");

        for (Bill p : bills) {
            System.out.printf("%-22s%-22s%-22o%-22s%-22s%-22s%n", p.getId(), p.getType(), p.getAmount(), p.getDueDate(), p.getState(), p.getProvider());
        }
        System.out.println("============================================================================================================================");
    }

}
