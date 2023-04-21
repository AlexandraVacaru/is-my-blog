package com.unibuc.ismyblog.service;

import com.unibuc.ismyblog.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
    void save(User user);
    void saveWithoutHash(User user);
    User findByUsername(String username);
    User getAuthenticatedUser();
    User findById(Long userId);
    Page<User> findPaginated(Pageable pageable);

    void deleteById(Long userId);
}
