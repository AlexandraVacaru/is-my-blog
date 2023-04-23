package com.unibuc.ismyblog.repository;

import com.unibuc.ismyblog.model.Blog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BlogRepository extends JpaRepository<Blog, Long> {
    @Query(nativeQuery = true, value = "SELECT * FROM blog WHERE LOWER(title) LIKE %:searchInput%")
    Page<Blog> findByTitle(@Param("searchInput") String searchInput, Pageable pageable);

    @Query(nativeQuery = true, value = "SELECT COUNT(*) FROM user_like WHERE blog_id = :blogId")
    Long getNoLikes(@Param("blogId") Long blogId);
}
