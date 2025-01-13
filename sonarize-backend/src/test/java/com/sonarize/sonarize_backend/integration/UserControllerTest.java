package com.sonarize.sonarize_backend.integration;

import com.sonarize.sonarize_backend.controller.UserController;
import com.sonarize.sonarize_backend.model.User;
import com.sonarize.sonarize_backend.service.UserService;
import com.sonarize.sonarize_backend.util.TestDataUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    void testGetUserById() throws Exception {
        User testUser = TestDataUtil.createTestUser();
        when(userService.getUserById("5ef6aa61-9327-458f-9ad2-0dba4bed5ab8"))
                .thenReturn(java.util.Optional.of(testUser));

        mockMvc.perform(get("/api/users/{userId}", "5ef6aa61-9327-458f-9ad2-0dba4bed5ab8"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("Wojciech"))
                .andExpect(jsonPath("$.spotifyId").value("461b35iwd06kspq5tc42o47dr"));

        verify(userService, times(1)).getUserById("5ef6aa61-9327-458f-9ad2-0dba4bed5ab8");
    }
}
