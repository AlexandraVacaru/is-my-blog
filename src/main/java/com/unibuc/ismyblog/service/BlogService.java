package com.unibuc.ismyblog.service;

import com.unibuc.ismyblog.model.Blog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

public interface BlogService {

    Blog save(Blog blog);

    Object findById(Long blogId);

    Page<Blog> findPaginated(Pageable pageable);

    void deleteById(Long blogId);
}
