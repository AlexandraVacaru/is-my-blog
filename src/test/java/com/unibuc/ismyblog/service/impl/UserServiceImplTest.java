package com.unibuc.ismyblog.service.impl;

import com.unibuc.ismyblog.exception.NotFoundException;
import com.unibuc.ismyblog.model.Role;
import com.unibuc.ismyblog.model.RoleEnum;
import com.unibuc.ismyblog.model.User;
import com.unibuc.ismyblog.repository.RoleRepository;
import com.unibuc.ismyblog.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.core.Authentication;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    UserServiceImpl userService;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Mock
    private SecurityContext securityContext;


    @Test
    void findByUsernameHappyFlow() {
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

    @Test
    void findByUsernameThrowsException() {

        String username = "username1";

        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        NotFoundException result = assertThrows(NotFoundException.class, () -> userService.findByUsername(username));

        assertNotNull(result);
        assertEquals("User with username: " + username + "not found", result.getMessage());
    }

    @Test
    void saveHappyFlow() {
        Role userRole = new Role(RoleEnum.ROLE_USER);
        User user = User.builder()
                .username("user1")
                .password("password1")
                .build();
        Set<Role> roles = new HashSet<>();
        roles.add(userRole);
        user.setRoles(roles);

        when(roleRepository.findAll()).thenReturn(new ArrayList<>(roles));
        when(bCryptPasswordEncoder.encode(any(String.class))).thenReturn("encryptedpassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        userService.save(user);

        verify(bCryptPasswordEncoder, times(1)).encode("password1");
        verify(roleRepository, times(1)).findAll();
        verify(userRepository, times(1)).save(user);

        assertEquals("encryptedpassword", user.getPassword());
        assertEquals(1, user.getRoles().size());

    }

    @Test
    void getAuthenticated() {
        User user = User.builder()
                .username("user1")
                .password("password1")
                .build();

        Authentication authentication = new TestingAuthenticationToken(user.getUsername(), user.getPassword());
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(authentication);

        when(userRepository.findByUsername(user.getUsername())).thenReturn(java.util.Optional.of(user));

        User authenticatedUser = userService.getAuthenticatedUser();

        verify(userRepository, times(1)).findByUsername(user.getUsername());

        assertEquals(user.getUsername(), authenticatedUser.getUsername());
        assertEquals(user.getUsername(), authenticatedUser.getUsername());
        assertEquals(user.getPassword(), authenticatedUser.getPassword());
    }

    @Test
    void findByIdHappyFlow() {
        User user = User.builder()
                .userId(10L)
                .username("user1")
                .password("pass1")
                .build();

        when(userRepository.findById(user.getUserId())).thenReturn(Optional.of(user));

        User result = userService.findById(user.getUserId());

        assertNotNull(result);
        assertEquals(user.getUserId(), result.getUserId());
    }

    @Test
    void findByIdThrowsException() {
        User user = User.builder()
                .userId(10L)
                .username("user1")
                .password("pass1")
                .build();

        when(userRepository.findById(user.getUserId())).thenReturn(Optional.empty());

        NotFoundException result = assertThrows(NotFoundException.class, () -> userService.findById(user.getUserId()));

        assertNotNull(result);
        assertEquals("User with userId: " + user.getUserId() + "not found", result.getMessage());

    }
}
