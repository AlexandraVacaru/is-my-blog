package com.unibuc.ismyblog.service.impl;

import com.unibuc.ismyblog.exception.NotFoundException;
import com.unibuc.ismyblog.model.Blog;
import com.unibuc.ismyblog.repository.BlogRepository;
import com.unibuc.ismyblog.repository.UserRepository;
import com.unibuc.ismyblog.service.BlogService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service("blogService")
@AllArgsConstructor
@Slf4j
public class BlogServiceImpl implements BlogService {

    private final BlogRepository blogRepository;
    private final UserRepository userRepository;

    @Override
    public List<Blog> findAll() {
        List<Blog> blogs = new LinkedList<>();
        blogRepository.findAll().iterator().forEachRemaining(blogs::add);
        return blogs;
    }

    @Override
    public Blog save(Blog blog) {
        return blogRepository.save(blog);
    }

    @Override
    public Page<Blog> findPaginated(Pageable pageable) {
        return blogRepository.findAll(pageable);
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
    public void deleteById(Long blogId) {
        Optional<Blog> blogOptional = blogRepository.findById(blogId);
        if(blogOptional.isEmpty()) {
            throw new NotFoundException("Blog with id: " + blogId + "not found");
        }

        Blog blog = blogOptional.get();
        blogRepository.save(blog);
        blogRepository.deleteById(blogId);
        log.info("Successfully deleted blog with id {}", blogId);
    }

    @Override
    public List<Blog> findLastPosted() {
        List<Blog> allBlogsSorted = findAll().stream()
                .sorted(Comparator.comparing(Blog::getDate))
                .collect(Collectors.toList());
        if (allBlogsSorted.size() >= 3 ) {
            return allBlogsSorted.subList(0,3);
        }
        return allBlogsSorted;
    }

    @Override
    public Page<Blog> findByTitle(String searchInput, Pageable pageable) {
        return blogRepository.findByTitle(searchInput, pageable);
    }
}
