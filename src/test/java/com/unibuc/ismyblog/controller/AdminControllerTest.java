package com.unibuc.ismyblog.controller;

import com.unibuc.ismyblog.model.Blog;
import com.unibuc.ismyblog.model.CategoryEnum;
import com.unibuc.ismyblog.model.User;
import com.unibuc.ismyblog.service.BlogService;
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
import java.time.LocalDateTime;
import java.util.ArrayList;
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

    @MockBean
    BlogService blogService;

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
    @WithMockUser(username = "user_1", password = "user", roles = "ADMIN")
    void blogsList() throws Exception {
        Blog blog1 = Blog.builder()
                .blogId(1L)
                .category(CategoryEnum.OTHER)
                .title("Blog1")
                .pictures(new ArrayList<>())
                .content("Content blog 1")
                .date(LocalDateTime.now())
                .build();
        Blog blog2 = Blog.builder()
                .blogId(2L)
                .category(CategoryEnum.OTHER)
                .pictures(new ArrayList<>())
                .title("Blog2")
                .content("Content blog 2")
                .date(LocalDateTime.now())
                .build();

        List<Blog> blogs = List.of(blog1, blog2);

        Page<Blog> blogPage = new PageImpl<>(blogs, PageRequest.of(1,
                4), blogs.size());

        when(blogService.findPaginated(any())).thenReturn(blogPage);

        mockMvc.perform(get("/admin/blog/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin-blogs"))
                .andExpect(model().attribute("blogPage", blogPage))
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
