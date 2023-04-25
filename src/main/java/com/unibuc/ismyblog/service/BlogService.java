package com.unibuc.ismyblog.service;

import com.unibuc.ismyblog.model.Blog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BlogService {

    List<Blog> findAll();
    Blog save(Blog blog);
    Page<Blog> findPaginated(Pageable pageable);
    Blog findById(Long blogId);
    void deleteById(Long blogId);
    List<Blog> findLastPosted();
    Page<Blog> findByTitle(String searchInput, Pageable pageable);
}
