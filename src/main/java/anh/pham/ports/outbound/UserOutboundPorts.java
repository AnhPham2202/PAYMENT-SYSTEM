package anh.pham.ports.outbound;

import anh.pham.domain.model.User;

public interface UserOutboundPorts {
    User addFund(Integer amount);
    Integer getBalanceByUserId(Long id);
    void updateBalance(Integer newAmount);
    User getCurrentUser();
}
