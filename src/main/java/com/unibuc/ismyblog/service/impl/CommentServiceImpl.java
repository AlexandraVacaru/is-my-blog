package com.unibuc.ismyblog.service.impl;

import com.unibuc.ismyblog.exception.NotFoundException;
import com.unibuc.ismyblog.model.Comment;
import com.unibuc.ismyblog.repository.CommentRepository;
import com.unibuc.ismyblog.service.CommentService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;

    @Override
    public Comment save(Comment comment) {
        return commentRepository.save(comment);
    }

    @Override
    public Comment findById(Long commentId) {
        Optional<Comment> commentOptional = commentRepository.findById(commentId);
        if (commentOptional.isEmpty()) {
            log.info("Comment with id {} was not found", commentId);
            throw new NotFoundException("Comment not found");
        }
        return commentOptional.get();
    }

    @Override
    public void deleteById(Long commentId) {
        Optional<Comment> commentOptional = commentRepository.findById(commentId);
        if (commentOptional.isEmpty()) {
            log.info("Comment with id {} was not found", commentId);
            throw new NotFoundException("Comment not found");
        }
        commentRepository.delete(commentOptional.get());
    }
}
