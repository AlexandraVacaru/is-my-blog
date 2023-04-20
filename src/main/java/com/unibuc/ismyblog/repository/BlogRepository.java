package com.unibuc.ismyblog.repository;

import com.unibuc.ismyblog.model.Blog;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlogRepository extends PagingAndSortingRepository<Blog, Long> {
}
