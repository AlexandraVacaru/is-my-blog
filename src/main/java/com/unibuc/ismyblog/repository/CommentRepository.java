package com.unibuc.ismyblog.repository;

import com.unibuc.ismyblog.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
