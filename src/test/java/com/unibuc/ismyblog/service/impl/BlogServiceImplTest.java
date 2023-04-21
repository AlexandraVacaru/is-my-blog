package com.unibuc.ismyblog.service.impl;

import com.unibuc.ismyblog.exception.NotFoundException;
import com.unibuc.ismyblog.model.Blog;
import com.unibuc.ismyblog.model.CategoryEnum;
import com.unibuc.ismyblog.repository.BlogRepository;
import com.unibuc.ismyblog.service.BlogService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BlogServiceImplTest {

    @InjectMocks
    BlogServiceImpl blogService;

    @Mock
    BlogRepository blogRepository;

    @Test
    void findByIdHappyFlow() {
        Blog blog = Blog.builder()
                .blogId(1L)
                .category(CategoryEnum.OTHER)
                .title("Blog1")
                .pictures(new ArrayList<>())
                .build();

        when(blogRepository.findById(blog.getBlogId())).thenReturn(java.util.Optional.of(blog));

        Blog result = blogService.findById(blog.getBlogId());

        assertNotNull(result);
        assertEquals(blog.getTitle(), result.getTitle());
        verify(blogRepository, times(1)).findById(blog.getBlogId());

    }

    @Test
    void findByIdThrowsException() {
        Long blogId = 1L;
        when(blogRepository.findById(blogId)).thenReturn(Optional.empty());

        NotFoundException result = assertThrows(NotFoundException.class, () -> blogService.findById(blogId));

        assertNotNull(result);
        assertEquals("Blog with id " + blogId + "not found!", result.getMessage());
    }

    @Test
    void save() {
        Blog blog = Blog.builder()
                .blogId(10L)
                .title("Blog10")
                .content("content blog 10")
                .build();

        when(blogRepository.save(any(Blog.class))).thenReturn(blog);

        Blog savedBlog = blogService.save(blog);

        verify(blogRepository, times(1)).save(blog);

        assertEquals(blog.getBlogId(), savedBlog.getBlogId());
        assertEquals(blog.getTitle(), savedBlog.getTitle());
        assertEquals(blog.getContent(), savedBlog.getContent());
    }
}
