package anh.pham.adapters.outbound.persistence;

import anh.pham.adapters.outbound.repository.UserRepository;
import anh.pham.domain.model.User;
import anh.pham.ports.outbound.UserOutboundPorts;

public class UserPersistenceAdapter implements UserOutboundPorts {
    private final UserRepository userRepository;

    public UserPersistenceAdapter(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User addFund(Integer amount)  {
        User currentUser = getCurrentUser();
        currentUser.setBalance(currentUser.getBalance() + amount);
        return userRepository.update(currentUser);
    }

    @Override
    public Integer getBalanceByUserId(Long id) {
        return userRepository.findById(id).getBalance();
    }

    @Override
    public void updateBalance(Integer newAmount) {
        User currentUser = getCurrentUser();
        currentUser.setBalance(newAmount);
        userRepository.update(currentUser);
    }


    // THIS HARD CODE ACTS AS A SECURITY CONTEXT
    @Override
    public User getCurrentUser() {
        return userRepository.findById(1L);
    }

}
