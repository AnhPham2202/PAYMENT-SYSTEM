package anh.pham.ports.inbound;

import anh.pham.domain.model.User;

public interface UserInboundPorts {
    User addFund(Integer amount);
    User getCurrentUser();
    Integer getBalance();
    void updateBalance(Integer newAmount);
}
