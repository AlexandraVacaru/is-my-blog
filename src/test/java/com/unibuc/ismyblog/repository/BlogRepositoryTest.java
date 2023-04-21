package com.unibuc.ismyblog.repository;


import com.unibuc.ismyblog.exception.NotFoundException;
import com.unibuc.ismyblog.model.Blog;
import com.unibuc.ismyblog.model.CategoryEnum;
import com.unibuc.ismyblog.model.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(scripts = "schema.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Slf4j
public class BlogRepositoryTest {

    @Autowired
    private BlogRepository blogRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    @Order(1)
    public void addBlog() {
        Optional<User> user = userRepository.findById(1000L);
        if(user.isEmpty()) {
            throw new NotFoundException("user not found");
        }
        Blog blog = Blog.builder()
                .blogId(1L)
                .title("test")
                .content("BlogContent")
                .category(CategoryEnum.OTHER)
                .date(LocalDateTime.now())
                .user(user.get())
                .build();
        Blog result = blogRepository.save(blog);
        assertNotNull(result);
        assertEquals(blog.getTitle(), result.getTitle());
    }
}
