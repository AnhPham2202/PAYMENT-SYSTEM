package anh.pham.adapters.inbound.request;

import anh.pham.domain.model.User;
import anh.pham.ports.inbound.UserInboundPorts;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.InputMismatchException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserRequestAdapterTest {
    User user;

    @InjectMocks
    UserRequestAdapter userRequestAdapter;

    @Mock
    UserInboundPorts userInput;
    private ByteArrayInputStream testIn;
    private ByteArrayOutputStream testOut;
    private void provideInput(String data) {
        testIn = new ByteArrayInputStream(data.getBytes());
        System.setIn(testIn);
    }

    private void provideOutput() {
        testOut = new ByteArrayOutputStream();

        System.setOut(new PrintStream(testOut));
    }

    @BeforeEach
    void setup() {
        user = new User(1L, "anh pham", 100_000);
    }


    @Test
    void testAddFund_whenInputNormalInteger_thenPrintCurrentAmount() {
        int amount = 2_000_000;
        when(userInput.addFund(amount)).thenReturn(new User(user.getId(), user.getName(), user.getBalance() + amount));
        // Act
        provideInput(Integer.toString(amount));

        provideOutput();
        userRequestAdapter.addFund();

        // Assert
        int total = user.getBalance() + amount;
        assertEquals("Enter amount: \nYour current available balance: " + total + "\n", testOut.toString().replace("\r",""));
    }


    @Test
    void testAddFund_whenInputNotNumber_thenPrintAwareness() {
        String str = "abc";
        // Act
        provideInput(str);

        // Assert
        assertThrows(InputMismatchException.class, () -> userRequestAdapter.addFund()
        );
    }}
