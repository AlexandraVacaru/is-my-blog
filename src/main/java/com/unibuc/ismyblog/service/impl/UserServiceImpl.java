package com.unibuc.ismyblog.service.impl;

import com.unibuc.ismyblog.exception.NotFoundException;
import com.unibuc.ismyblog.model.Role;
import com.unibuc.ismyblog.model.RoleEnum;
import com.unibuc.ismyblog.model.User;
import com.unibuc.ismyblog.repository.RoleRepository;
import com.unibuc.ismyblog.repository.UserRepository;
import com.unibuc.ismyblog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public void save(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        Set<Role> userRoles = roleRepository.findAll()
                .stream()
                .filter(x -> x.getName().equals(RoleEnum.ROLE_USER))
                .collect(Collectors.toSet());
        if (userRoles.isEmpty()) {
            Role role = roleRepository.save(new Role(RoleEnum.ROLE_USER));
            userRoles.add(role);
        }
        user.setRoles(userRoles);
        userRepository.save(user);
    }

    @Override
    public void saveWithoutHash(User user) {
        userRepository.save(user);
    }

    @Override
    public User findByUsername(String username) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        if(userOptional.isEmpty()) {
            throw new NotFoundException("User with username: " +  username + "not found");
        }
        return userOptional.get();
    }

    @Override
    public User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return findByUsername(authentication.getName());
    }

    @Override
    public User findById(Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if(userOptional.isEmpty()) {
            throw new NotFoundException("User with userId: " +  userId + "not found");
        }
        return userOptional.get();
    }


}