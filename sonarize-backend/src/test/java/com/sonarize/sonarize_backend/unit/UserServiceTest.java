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

    private User createTestUser() {
        User user = new User();
        user.setId("5ef6aa61-9327-458f-9ad2-0dba4bed5ab8");
        user.setSpotifyId("461b35iwd06kspq5tc42o47dr");
        user.setUsername("Wojciech");
        return user;
    }

    @Test
    void testCreateUser() {
        User user = createTestUser();
        when(userRepository.save(any(User.class))).thenReturn(user);

        User createdUser = userService.createUser(user);

        assertEquals("Wojciech", createdUser.getUsername());
        assertEquals("5ef6aa61-9327-458f-9ad2-0dba4bed5ab8", createdUser.getId());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testFindUserByUsername() {
        User user = createTestUser();
        when(userRepository.findByUsername("Wojciech")).thenReturn(user);

        User foundUser = userService.findUserByUsername("Wojciech");

        assertEquals("Wojciech", foundUser.getUsername());
        assertEquals("461b35iwd06kspq5tc42o47dr", foundUser.getSpotifyId());
        verify(userRepository, times(1)).findByUsername("Wojciech");
    }

    @Test
    void testGetUserById() {
        User user = createTestUser();
        when(userRepository.findById("5ef6aa61-9327-458f-9ad2-0dba4bed5ab8")).thenReturn(Optional.of(user));

        Optional<User> foundUser = userService.getUserById("5ef6aa61-9327-458f-9ad2-0dba4bed5ab8");

        assertTrue(foundUser.isPresent());
        assertEquals("5ef6aa61-9327-458f-9ad2-0dba4bed5ab8", foundUser.get().getId());
        verify(userRepository, times(1)).findById("5ef6aa61-9327-458f-9ad2-0dba4bed5ab8");
    }
}
