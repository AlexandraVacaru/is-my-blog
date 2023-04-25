package com.unibuc.ismyblog.controller;

import com.unibuc.ismyblog.model.Blog;
import com.unibuc.ismyblog.model.Comment;
import com.unibuc.ismyblog.model.ContactInfo;
import com.unibuc.ismyblog.model.User;
import com.unibuc.ismyblog.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

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

    @GetMapping("/user/{username}")
    public ModelAndView getUserByUsername(@PathVariable("username") String username){
        ModelAndView modelAndView = new ModelAndView("user-profile");
        modelAndView.addObject("user", userService.findByUsername(username));
        return modelAndView;
    }

    @PreAuthorize("#username == authentication.principal.username or hasRole('ROLE_ADMIN')")
    @GetMapping("/user/edit/{username}")
    public ModelAndView editUser(@PathVariable("username") String username){
        ModelAndView modelAndView = new ModelAndView("edit-user-profile");
        modelAndView.addObject("user", userService.findByUsername(username));
        return modelAndView;
    }

    @PostMapping("/user/edit")
    public String editUser(@ModelAttribute @Valid User user, BindingResult bindingResult){

        if (bindingResult.hasErrors()) {
            return "edit-user-profile";
        }
        User dbUser = userService.findById(user.getUserId());
        user.setRoles(dbUser.getRoles());
        user.setContactInfo(dbUser.getContactInfo());
        userService.saveWithoutHash(user);
        log.info("User {} updated", user.getUsername());
        return "redirect:/user/" + user.getUsername();
    }

    @GetMapping("user/contactInfo/{username}")
    @PreAuthorize("#username == authentication.principal.username or hasRole('ROLE_ADMIN')")
    public ModelAndView newContactInfo(@PathVariable String username) {
        ContactInfo contactInfo = new ContactInfo();
        contactInfo.setUser(userService.findByUsername(username));

        ModelAndView modelAndView = new ModelAndView("add-contact-info");
        modelAndView.addObject("contactInfo", contactInfo);
        return modelAndView;
    }

    @GetMapping("user/contactInfo/update/{contactInfoId}")
    public String editContactInfo(@PathVariable("contactInfoId") Long contactInfoId, Model model) {
        User authenticatedUser = userService.getAuthenticatedUser();
        ContactInfo contactInfo = contactInfoService.findById(contactInfoId);
        if(!Objects.equals(authenticatedUser, contactInfo.getUser())) {
            throw new AccessDeniedException("Access is denied");
        }
        model.addAttribute("contactInfo",contactInfoService.findById(contactInfoId));
        return "add-contact-info";
    }

    @PostMapping("user/contactInfo")
    public String addContactInfo(@ModelAttribute("contactInfo") @Valid ContactInfo contactInfo,
                                 BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "add-contact-info";
        }
        User user = userService.findByUsername(contactInfo.getUser().getUsername());
        if(user.getContactInfo() != null) {
            contactInfoService.deleteContactInfo(user.getContactInfo().getContactInfoId());
        }
        ContactInfo savedContactInfo = contactInfoService.save(contactInfo);
        user.setContactInfo(savedContactInfo);
        userService.saveWithoutHash(user);
        log.info("Added contact info details for user {}", contactInfo.getUser().getUsername());

        return "redirect:/user/" + contactInfo.getUser().getUsername();
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
}
