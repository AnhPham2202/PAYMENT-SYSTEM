package anh.pham.domain.service;

import anh.pham.domain.model.User;
import anh.pham.exception.InvalidInformation;
import anh.pham.ports.inbound.UserInboundPorts;
import anh.pham.ports.outbound.UserOutboundPorts;

public class UserService implements UserInboundPorts {
    private final UserOutboundPorts userOutbound;

    public UserService(UserOutboundPorts userOutbound) {
        this.userOutbound = userOutbound;
    }

    @Override
    public User addFund(Integer amount) {
        if (amount <= 0) throw new InvalidInformation("Amount should be greater than 0 !!!");
        return userOutbound.addFund(amount);
    }

    @Override
    public User getCurrentUser() {
        return userOutbound.getCurrentUser();
    }

    @Override
    public Integer getBalance() {
        return userOutbound.getBalanceByUserId(getCurrentUser().getId());
    }

    @Override
    public void updateBalance(Integer newAmount) {
        userOutbound.updateBalance(newAmount);
    }

}
