package com.unibuc.ismyblog.controller;

import com.unibuc.ismyblog.model.User;
import com.unibuc.ismyblog.service.RoleService;
import com.unibuc.ismyblog.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("h2")
public class AdminControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserService userService;

    @MockBean
    RoleService roleService;

    @Test
    @WithMockUser(username = "user_1", password = "user", roles = "ADMIN")
    void usersList() throws Exception {
        User user1 = User.builder()
                .username("user1")
                .email("user1@yahoo.com")
                .password("password")
                .birthDate(LocalDate.parse("1999-09-18"))
                .lastName("user")
                .firstName("user")
                .build();
        User user2 = User.builder()
                .username("user2")
                .email("user2@yahoo.com")
                .password("password")
                .birthDate(LocalDate.parse("1999-09-18"))
                .lastName("user")
                .firstName("user")
                .build();
        List<User> users = List.of(user1, user2);
        Page<User> userPage = new PageImpl<>(users, PageRequest.of(1,
                4), users.size());

        when(userService.findPaginated(any())).thenReturn(userPage);

        mockMvc.perform(get("/user/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("users"))
                .andExpect(model().attribute("userPage", userPage))
                .andExpect(content().contentType("text/html;charset=UTF-8"));
    }

    @Test
    @WithMockUser(username = "user_1", password = "user", roles = "USER")
    public void deleteByIdIsForbidden() throws Exception {

        mockMvc.perform(get("/user/delete/{userId}", "2"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "user_1", password = "user", roles = "ADMIN")
    public void deleteByIdMockMvc() throws Exception {

        mockMvc.perform(get("/user/delete/{userId}", "2"))
                .andExpect(redirectedUrl("/user/list"));
    }

}
