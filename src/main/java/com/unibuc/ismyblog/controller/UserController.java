package com.unibuc.ismyblog.controller;

import com.unibuc.ismyblog.model.User;
import com.unibuc.ismyblog.service.ContactInfoService;
import com.unibuc.ismyblog.service.SecurityService;
import com.unibuc.ismyblog.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.time.LocalDate;

@Controller
@Slf4j
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private SecurityService securityService;

    @Autowired
    private ContactInfoService contactInfoService;

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

    @GetMapping({"/", "/index"})
    public ModelAndView welcome(){
        ModelAndView modelAndView = new ModelAndView("welcome");
        modelAndView.addObject("authenticatedUser", userService.getAuthenticatedUser());
        return modelAndView;
    }
}
