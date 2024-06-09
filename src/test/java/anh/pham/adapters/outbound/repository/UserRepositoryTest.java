package anh.pham.adapters.outbound.repository;

import anh.pham.domain.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class UserRepositoryTest {
    UserRepository userRepository = new UserRepository();

    @Test
    public void testFindById_whenFindOut_thenReturnThatUser() {
        User user = userRepository.findById(1L);
        assertNotNull(user);
        assertEquals(1L, user.getId());

    }

    @Test
    public void testUpdate_whenUserAvailable_thenUpdate() {
        User user = userRepository.findById(1L);
        user.setBalance(8888);
        userRepository.update(user);
        user = userRepository.findById(1L);
        assertEquals(8888, user.getBalance());
    }

    @Test
    public void testUpdate_whenUserNotAvailable_thenCreate() {
        User user = new User(2L, "Anh Pham Mock", 99999);
        userRepository.update(user);
        user = userRepository.findById(2L);
        assertEquals(2L, user.getId());
        assertEquals(99999, user.getBalance());
        assertEquals("Anh Pham Mock", user.getName());
    }


}
