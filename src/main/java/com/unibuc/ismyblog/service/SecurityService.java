package com.unibuc.ismyblog.service;

public interface SecurityService {
    boolean isAuthenticated();
    void autoLogin(String username, String password);
}
