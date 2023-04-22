package com.unibuc.ismyblog.service.impl;

import com.unibuc.ismyblog.exception.NotFoundException;
import com.unibuc.ismyblog.model.Blog;
import com.unibuc.ismyblog.model.User;
import com.unibuc.ismyblog.repository.BlogRepository;
import com.unibuc.ismyblog.service.BlogService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;

@Service("blogService")
@AllArgsConstructor
@Slf4j
public class BlogServiceImpl implements BlogService {

    private final BlogRepository blogRepository;

    @Override
    public Blog save(Blog blog) {
        return blogRepository.save(blog);
    }

    @Override
    public Blog findById(Long blogId) {
        Optional<Blog> blogOptional = blogRepository.findById(blogId);
        if(blogOptional.isEmpty()) {
            log.info("Error when trying to find the blog with id {}", blogId);
            throw new NotFoundException("Blog with id " + blogId + "not found!");
        }
        log.info("Returned blog with id {}", blogId);
        return blogOptional.get();
    }

    @Override
    public Page<Blog> findPaginated(Pageable pageable) {
        return blogRepository.findAll(pageable);
    }

    @Override
    public void deleteById(Long blogId) {
        Optional<Blog> blogOptional = blogRepository.findById(blogId);
        if(blogOptional.isEmpty()) {
            throw new NotFoundException("Blog with id: " + blogId + "not found");
        }

        Blog blog = blogOptional.get();
        Set<User> users = new LinkedHashSet<>();

        blogRepository.save(blog);
        blogRepository.deleteById(blogId);
        log.info("Successfully deleted blog with id {}", blogId);
    }
}
