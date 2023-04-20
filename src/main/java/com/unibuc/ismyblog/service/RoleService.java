package com.unibuc.ismyblog.service;

import com.unibuc.ismyblog.model.Role;

import java.util.List;
import java.util.Optional;

public interface RoleService {
    List<Role> findAllRoles();
    Optional<Role> findById(Long id);
}
