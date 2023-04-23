package com.unibuc.ismyblog.controller;

import com.unibuc.ismyblog.exception.NotFoundException;
import com.unibuc.ismyblog.model.Blog;
import com.unibuc.ismyblog.model.Comment;
import com.unibuc.ismyblog.model.User;
import com.unibuc.ismyblog.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Controller
@Slf4j
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private SecurityService securityService;

    @Autowired
    private ContactInfoService contactInfoService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private BlogService blogService;

    @GetMapping("/registration")
    public String registration(Model model) {
        if (securityService.isAuthenticated()) {
            return "redirect:/";
        }

        model.addAttribute("userForm", new User());

        return "registration";
    }

    @PostMapping("/registration")
    public String registration(@Valid @ModelAttribute("userForm") User userForm, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "registration";
        }

        userService.save(userForm);

        securityService.autoLogin(userForm.getUsername(), userForm.getPassword());
        log.info("User with username {} has successfully registered on {}", userForm.getUsername(), LocalDate.now());

        return "redirect:/welcome";
    }

    @GetMapping("/login")
    public String showLoginForm() {
        if(securityService.isAuthenticated()) {
            return "redirect:/index";
        }
        return "login";
    }

    @PostMapping("comment/{blogId}/user/{username}")
    public String addComment(@ModelAttribute("comment") @Valid Comment comment,
                             BindingResult bindingResult,
                             @PathVariable("blogId") Long blogId,
                             @PathVariable("username") String username,
                             Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("blog", blogService.findById(blogId));
            model.addAttribute("lastPosted", blogService.findLastPosted());
            return "blog";
        }

        Blog blog = blogService.findById(blogId);
        User user = userService.findByUsername(username);

        comment.setBlog(blog);
        comment.setUser(user);
        comment.setDate(LocalDateTime.now());
        commentService.save(comment);
        log.info("User {} added a new comment for blog {}", user.getUsername(), blog.getBlogId());
        return "redirect:/blog/" + blogId;
    }

    @RequestMapping("like/{blogId}/user/{username}")
    public String likeBlog(@PathVariable("blogId") Long blogId, @PathVariable("username") String username) {
        Blog blog = blogService.findById(blogId);
        User user = userService.findByUsername(username);

        user.getLikedBlogs().add(blog);
        userService.saveWithoutHash(user);
        log.info("User {} liked blog with id {}", username, blogId);
        return "redirect:/blog/" + blogId;

    }

    @RequestMapping("removeLike/{blogId}/user/{username}")
    public String removeLikeBlog(@PathVariable("blogId") Long blogId, @PathVariable("username") String username) {
        Blog blog = blogService.findById(blogId);
        User user = userService.findByUsername(username);

        if(blogService.isLiked(blogId, username)) {
            user.getLikedBlogs().remove(blog);
            userService.saveWithoutHash(user);
        } else {
            throw new NotFoundException("Like not found!");
        }
        log.info("User {} removed like from blog with id {}", username, blogId);
        return "redirect:/blog/" + blogId;

    }

    @GetMapping("/user/{username}")
    public ModelAndView getUserByUsername(@PathVariable("username") String username){
        ModelAndView modelAndView = new ModelAndView("user-profile");
        modelAndView.addObject("user", userService.findByUsername(username));
        return modelAndView;
    }

}
