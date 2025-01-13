package com.sonarize.sonarize_backend.unit;

import com.sonarize.sonarize_backend.model.User;
import com.sonarize.sonarize_backend.repository.UserRepository;
import com.sonarize.sonarize_backend.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    public UserServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateUser() {
        User user = new User();
        user.setUsername("testUser");
        when(userRepository.save(any(User.class))).thenReturn(user);

        User createdUser = userService.createUser(user);

        assertEquals("testUser", createdUser.getUsername());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testFindUserByUsername() {
        User user = new User();
        user.setUsername("testUser");
        when(userRepository.findByUsername("testUser")).thenReturn(user);

        User foundUser = userService.findUserByUsername("testUser");

        assertEquals("testUser", foundUser.getUsername());
        verify(userRepository, times(1)).findByUsername("testUser");
    }

    @Test
    void testGetUserById() {
        User user = new User();
        user.setId("123");
        when(userRepository.findById("123")).thenReturn(Optional.of(user));

        Optional<User> foundUser = userService.getUserById("123");

        assertTrue(foundUser.isPresent());
        assertEquals("123", foundUser.get().getId());
        verify(userRepository, times(1)).findById("123");
    }
}
