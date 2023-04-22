package com.unibuc.ismyblog.controller;

import com.unibuc.ismyblog.model.Blog;
import com.unibuc.ismyblog.model.CategoryEnum;
import com.unibuc.ismyblog.service.BlogService;
import com.unibuc.ismyblog.service.ImageService;
import com.unibuc.ismyblog.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Controller
@AllArgsConstructor
@Slf4j
public class BlogController {

    private final BlogService blogService;
    private final ImageService imageService;
    private final UserService userService;

    @GetMapping("/blog/new")
    public String newBlog(Model model) {
        model.addAttribute("blog", new Blog());
        List<CategoryEnum> categoriesAll = List.of(CategoryEnum.values());
        model.addAttribute("categoriesAll", categoriesAll );
        return "blog-form";
    }

    @PostMapping("/blog")
    public String save(@Valid @ModelAttribute("blog") Blog blog,
                       BindingResult bindingResult,
                       @RequestParam("images") MultipartFile[] images) throws IOException {
        if (bindingResult.hasErrors()){
            return "blog-form";
        }
        blog.setDate(LocalDateTime.now());
        blog.setUser(userService.getAuthenticatedUser());
        Blog savedBlog = blogService.save(blog);
        imageService.saveImageFile(savedBlog.getBlogId(), images);
        log.info("Successfully added blog with id {} by user {}", savedBlog.getBlogId(),
                savedBlog.getUser().getUsername());
        return "redirect:/blog/list" ;
    }

    @GetMapping("/blog/{blogId}")
    public String showById(@PathVariable("blogId") Long blogId, Model model){
        model.addAttribute("blog", blogService.findById(blogId));
        return "blog";
    }

    @PreAuthorize("#username == authentication.principal.username or hasRole('ROLE_ADMIN')")
    @RequestMapping("/blog/delete/{blogId}")
    public String deleteById(@PathVariable("blogId") Long blogId,
                             @RequestParam("username") String username,
                             @RequestParam("page") Optional<Integer> page,
                             @RequestParam("size") Optional<Integer> size){
        blogService.deleteById(blogId);
        log.info("User {} successfully deleted blog with id {}", username, blogId);
        if (size.isPresent() && page.isPresent()) {
            return "redirect:/admin/blog/list?size=" + size.get() + "&page=" + page.get();
        }
        return "redirect:/blog/list";
    }
}
