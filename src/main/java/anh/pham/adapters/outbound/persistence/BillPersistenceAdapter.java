package anh.pham.adapters.outbound.persistence;

import anh.pham.adapters.outbound.repository.BillRepository;
import anh.pham.domain.model.Bill;
import anh.pham.domain.model.User;
import anh.pham.exception.BadRequestException;
import anh.pham.exception.InvalidInformation;
import anh.pham.ports.inbound.UserInboundPorts;
import anh.pham.ports.outbound.BillOutboundPorts;

import java.util.Set;

public class BillPersistenceAdapter implements BillOutboundPorts {
    private final BillRepository billRepository;
    private final UserInboundPorts userInput;

    public BillPersistenceAdapter(BillRepository billRepository, UserInboundPorts userInput) {
        this.billRepository = billRepository;
        this.userInput = userInput;
    }

    @Override
    public Set<Bill> findAllByUserId(Long userId) {
        return billRepository.findAllByUserId(userId);
    }

    @Override
    public Set<Bill> updateBills(Set<Long> ids) throws InvalidInformation {
        Set<Bill> bills = billRepository.findByIds(ids);
        if (bills.size() != ids.size()) {
            throw new InvalidInformation("Invalid payment id !!!");
        }

        Integer totalAmount = bills.stream().mapToInt(Bill::getAmount).sum();
        Integer currentBalance = userInput.getBalance();
        if (totalAmount > currentBalance) {
            throw new BadRequestException("You balance not enough money to pay these bills !!!");
        }

        bills.forEach(p -> {
            p.setState("PAID");
        });

        userInput.updateBalance(currentBalance - totalAmount);
        return bills;
    }

    @Override
    public Set<Bill> getBillsPool() {
        return billRepository.getBillsPool();
    }

    @Override
    public Set<Bill> addBills(Set<Long> ids) throws InvalidInformation {
        User currentUser = userInput.getCurrentUser();
        Set<Bill> bills = billRepository.getBillsPoolByIds(ids);
        if (bills.size() != ids.size()) throw new InvalidInformation("Invalid bill ids !!!");
        billRepository.deleteBillsPool(bills);
        bills.forEach(p -> p.setUserId(currentUser.getId()));
        billRepository.addBills(bills);
        return bills;
    }

    @Override
    public Set<Bill> deleteBills(Set<Long> ids) {
        Set<Bill> bills = billRepository.findByIds(ids);
        if (bills.size() != ids.size()) throw new InvalidInformation("Invalid bill ids !!!");
        billRepository.delete(bills);
        bills.forEach(p -> p.setUserId(null));
        billRepository.addBillsPool(bills);
        return bills;
    }
}
