package anh.pham.domain.service;

import anh.pham.domain.model.User;
import anh.pham.exception.InvalidInformation;
import anh.pham.ports.outbound.UserOutboundPorts;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    User user;
    @InjectMocks
    UserService userService;
    @Mock
    UserOutboundPorts userOutbound;

    @BeforeEach
    public void setup() {
        user = new User(2L, "anh pham", 800_000);
    }

    @Test
    public void testAddFund_whenNormalNumber_thenAddFund() {
        // Arrange
        int amount = 2_000_000;
        when(userOutbound.addFund(ArgumentMatchers.anyInt())).thenReturn(user);

        // Act
        User user = userService.addFund(amount);

        // Assert
        int total = user.getBalance() + amount;
        verify(userOutbound, times(1)).addFund(amount);
        assertEquals(user.getBalance() + amount, total);
    }

    @Test
    public void testAddFund_whenNegativeNumber_thenThrowsException() {
        //Arrange
        int amount = -2_000_000;

        // Act
        InvalidInformation thrown = assertThrows(
                InvalidInformation.class,
                () -> userService.addFund(amount)
        );
        // Assert
        verify(userOutbound, never()).addFund(amount);
        assertEquals("Amount should be greater than 0 !!!", thrown.getMessage());
    }

    @Test
    public void testAddFund_whenZero_thenThrowsException() {
        //Arrange
        int amount = 0;

        // Act
        InvalidInformation thrown = assertThrows(
                InvalidInformation.class,
                () -> userService.addFund(amount)
        );
        // Assert
        verify(userOutbound, never()).addFund(amount);
        assertEquals("Amount should be greater than 0 !!!", thrown.getMessage());
    }

    @Test
    void testGetCurrentUser() {
        // Arrange
        when(userOutbound.getCurrentUser()).thenReturn(user);

        // Act
        User actualUser = userService.getCurrentUser();

        // Assert
        verify(userOutbound).getCurrentUser();
        assertSame(user, actualUser);
    }

    @Test
    void testGetBalance() {
        // Arrange
        when(userOutbound.getCurrentUser()).thenReturn(user);
        when(userOutbound.getBalanceByUserId(anyLong())).thenReturn(user.getBalance());

        // Act
        Integer currentBalance = userService.getBalance();

        // Assert
        verify(userOutbound, times(1)).getCurrentUser();
        verify(userOutbound, times(1)).getBalanceByUserId(anyLong());
        assertEquals(user.getBalance(), currentBalance);
    }


    @Test
    public void testUpdateBalance_whenNormalNumber_thenCallUpdateBalanceFromUserOutbound() {
        // Arrange
        int amount = 2_000_000;

        // Act
        userService.updateBalance(amount);

        // Assert
        verify(userOutbound, times(1)).updateBalance(amount);
    }

    @Test
    public void testUpdateBalance_whenNegativeNumber_thenKeepCallUpdateBalanceFromUserOutbound() {
        //Arrange
        int amount = -2_000_000;

        // Act
        userService.updateBalance(amount);

        // Assert
        verify(userOutbound, times(1)).updateBalance(amount);
    }

    @Test
    public void testUpdateBalance_whenZero_thenKeepCallUpdateBalanceFromUserOutbound() {
        //Arrange
        int amount = 0;

        // Act
        userService.updateBalance(amount);

        // Assert
        verify(userOutbound, times(1)).updateBalance(amount);
    }
}
