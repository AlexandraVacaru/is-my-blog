package com.unibuc.ismyblog.service.impl;

import com.unibuc.ismyblog.model.User;
import com.unibuc.ismyblog.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserServiceImpl userService;

    @Test
    void findByUsername() {
        User user = User.builder()
                .userId(10L)
                .username("user1")
                .password("pass1")
                .firstName("firstname1")
                .lastName("lastname1")
                .email("email1")
                .birthDate(LocalDate.parse("1999-09-18"))
                .build();

        when(userRepository.findByUsername(user.getUsername())).thenReturn(java.util.Optional.of(user));

        User result = userService.findByUsername(user.getUsername());

        assertNotNull(result);
        assertEquals(user.getUserId(), result.getUserId());
    }
}
