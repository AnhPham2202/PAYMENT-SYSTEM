package anh.pham.adapters.outbound.persistence;

import anh.pham.adapters.outbound.repository.UserRepository;
import anh.pham.domain.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserPersistenceAdapterTest {
    User user;
    @Mock
    UserRepository userRepository;
    @InjectMocks
    UserPersistenceAdapter userPersistenceAdapter;

    @BeforeEach
    public void setup() {
        user = new User(2L, "anh pham", 1_000_000);
    }

    @Test
    public void testAddFund_whenAddAlreadyValidAmount_thenUpdateSumAmountToBalance() {
        // Arrange
        int amount = 500_000;
        when(userRepository.findById(anyLong())).thenReturn(user);
        when(userRepository.update(user)).thenReturn(user);

        // Act
        User actualUser = userPersistenceAdapter.addFund(amount);

        // Assert
        verify(userRepository, times(1)).findById(anyLong());
        verify(userRepository, times(1)).update(user);
        assertEquals(user.getBalance(), actualUser.getBalance());
    }

    @Test
    public void testGetBalanceByUserId_whenAlreadyValidId_thenReturnBalance() {
        // Arrange
        when(userRepository.findById(anyLong())).thenReturn(user);

        // Act
        Integer balance = userPersistenceAdapter.getBalanceByUserId(anyLong());

        // Assert
        verify(userRepository, times(1)).findById(anyLong());
        assertEquals(user.getBalance(), balance);
    }

    @Test
    public void testUpdateBalance_whenUpdateAlreadyValidBalance_thenUpdateNewBalance() {
        // Arrange
        int newBalance = 500_000;
        when(userRepository.findById(anyLong())).thenReturn(user);
        when(userRepository.update(user)).thenReturn(user);

        // Act
        userPersistenceAdapter.updateBalance(newBalance);

        // Assert
        verify(userRepository, times(1)).findById(anyLong());
        verify(userRepository, times(1)).update(user);
        assertEquals(newBalance, user.getBalance());
    }

}
