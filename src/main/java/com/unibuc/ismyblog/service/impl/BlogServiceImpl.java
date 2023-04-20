package com.unibuc.ismyblog.service.impl;

import com.unibuc.ismyblog.model.Blog;
import com.unibuc.ismyblog.repository.BlogRepository;
import com.unibuc.ismyblog.service.BlogService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service("blogService")
@AllArgsConstructor
@Slf4j
public class BlogServiceImpl implements BlogService {

    private final BlogRepository blogRepository;

    @Override
    public Blog save(Blog blog) {
        return blogRepository.save(blog);
    }
}
