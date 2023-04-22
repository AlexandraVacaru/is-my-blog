package com.unibuc.ismyblog.service;

import com.unibuc.ismyblog.model.Blog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BlogService {

    Blog save(Blog blog);

    Blog findById(Long blogId);

    Page<Blog> findPaginated(Pageable pageable);

    void deleteById(Long blogId);

    List<Blog> findAll();

    Page<Blog> findPaginatedWelcome(Pageable pageable);
}
