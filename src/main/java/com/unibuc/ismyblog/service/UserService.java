package com.unibuc.ismyblog.service;

import com.unibuc.ismyblog.model.User;

public interface UserService {
    void save(User user);
    void saveWithoutHash(User user);
    User findByUsername(String username);
    User getAuthenticatedUser();
    User findById(Long userId);
}
