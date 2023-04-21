package com.unibuc.ismyblog.controller;

import com.unibuc.ismyblog.exception.NotFoundException;
import com.unibuc.ismyblog.model.Blog;
import com.unibuc.ismyblog.model.CategoryEnum;
import com.unibuc.ismyblog.model.User;
import com.unibuc.ismyblog.service.BlogService;
import com.unibuc.ismyblog.service.ImageService;
import com.unibuc.ismyblog.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@SpringBootTest
@AutoConfigureMockMvc
public class BlogControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    BlogService blogService;

    @MockBean
    UserService userService;

    @MockBean
    ImageService imageService;

    @Test
    @DisplayName("Show by id blog not found")
    @WithMockUser(username = "user_1", password = "user", roles = "USER")
    public void showByIdThrowsException() throws Exception {
        Long blogId = 100L;

        when(blogService.findById(blogId)).thenThrow(new NotFoundException("Blog not found!"));

        mockMvc.perform(get("/blog/{blogId}",blogId))
                .andExpect(status().isNotFound())
                .andExpect(view().name("notfound"));
    }


    @Test
    @DisplayName("Show by id happy flow")
    @WithMockUser(username = "user_1", password = "user", roles = "USER")
    public void showById() throws Exception {
        Long id = 1l;
        Blog blogTest = new Blog();
        blogTest.setBlogId(id);

        when(blogService.findById(id)).thenReturn(blogTest);

        mockMvc.perform(get("/blog/{blogId}", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("blog"))
                .andExpect(model().attribute("blog", blogTest))
                .andExpect(content().contentType("text/html;charset=UTF-8"));
    }

    @Test
    @WithMockUser(username = "user_1", password = "user", roles = "USER")
    void saveBlog() throws Exception {
        Blog blog = Blog.builder()
                .blogId(1L)
                .category(CategoryEnum.OTHER)
                .content("BlogContent")
                .title("Blog1")
                .pictures(new ArrayList<>())
                .build();
        User user = User.builder()
                .userId(1L)
                .email("email@yahoo.com")
                .password("12345")
                .birthDate(LocalDate.of(2020, 10, 10))
                .build();

        MockMultipartFile images
                = new MockMultipartFile(
                "images",
                "image.png",
                MediaType.IMAGE_PNG_VALUE,
                "Hello, World!".getBytes()
        );

        when(userService.getAuthenticatedUser()).thenReturn(user);
        when(blogService.save(blog)).thenReturn(blog);

        mockMvc.perform(multipart("/blog").file(images)
                        .contentType(MediaType.APPLICATION_JSON)
                        .flashAttr("blog", blog)
                        .with(csrf()))
                .andExpect(model().hasNoErrors())
                .andExpect(redirectedUrl("/blog/list"));

    }


    @Test
    @WithMockUser(username = "user_1", password = "user", roles = "USER")
    void saveBlogInvalid() throws Exception {
        Blog blog = Blog.builder()
                .blogId(1L)
                .category(CategoryEnum.OTHER)
                .title("Blog1")
                .pictures(new ArrayList<>())
                .build();
        User user = User.builder()
                .userId(1L)
                .email("email@yahoo.com")
                .password("12345")
                .birthDate(LocalDate.of(2020, 10, 10))
                .build();

        MockMultipartFile images
                = new MockMultipartFile(
                "images",
                "image.png",
                MediaType.IMAGE_PNG_VALUE,
                "Hello, World!".getBytes()
        );

        when(userService.getAuthenticatedUser()).thenReturn(user);
        when(blogService.save(blog)).thenReturn(blog);

        mockMvc.perform(multipart("/blog").file(images)
                        .contentType(MediaType.APPLICATION_JSON)
                        .flashAttr("blog", blog)
                        .with(csrf()))
                .andExpect(model().hasErrors())
                .andExpect(view().name("blog-form"));

    }
}
