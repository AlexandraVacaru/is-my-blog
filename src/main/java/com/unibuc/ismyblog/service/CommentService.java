package com.unibuc.ismyblog.service;

import com.unibuc.ismyblog.model.Comment;

public interface CommentService {

    Comment save(Comment comment);

    Comment findById(Long commentId);

    void deleteById(Long commentId);
}
