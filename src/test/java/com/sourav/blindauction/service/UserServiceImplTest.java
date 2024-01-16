package com.sourav.blindauction.service;

import com.sourav.blindauction.model.User;
import com.sourav.blindauction.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.anyString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Test
    public void testRegisterUser() {
        User user = new User();
        when(userRepository.save(any(User.class))).thenReturn(user);

        String result = userService.registerUser(user);
        verify(userRepository, times(1)).save(any(User.class));
        assertEquals("User registered successfully", result);
    }

    @Test
    public void testValidateToken() {
        String token = "testToken";
        User user = new User();
        when(userRepository.findByToken(anyString())).thenReturn(user);
        boolean result = userService.validateToken(token);
        verify(userRepository, times(1)).findByToken(anyString());
        assertTrue(result);
    }

    @Test
    public void testGetUserByToken() {
        String token = "testToken";
        User user = new User();
        when(userRepository.findByToken(anyString())).thenReturn(user);
        User result = userService.getUserByToken(token);
        verify(userRepository, times(1)).findByToken(anyString());
        assertEquals(user, result);
    }
}
