package com.unibuc.ismyblog.utils;

import com.unibuc.ismyblog.model.ContactInfo;
import com.unibuc.ismyblog.model.Role;
import com.unibuc.ismyblog.model.RoleEnum;
import com.unibuc.ismyblog.model.User;

import java.time.LocalDate;
import java.util.Set;

public class TestUtils {

    public static ContactInfo validContactInfo() {
        return ContactInfo.builder()
                .contactInfoId(1L)
                .city("Bucuresti")
                .country("Romania")
                .phoneNumber("0722test")
                .user(null)
                .build();
    }

    public static User validUser() {
        return User.builder()
                .userId(1L)
                .email("email@email.com")
                .username("test_user")
                .password("test_password")
                .picture("picture")
                .contactInfo(null)
                .lastName("test_user_last_name")
                .firstName("test_user_first_name")
                .birthDate(LocalDate.MAX)
                .roles(Set.of(validRole()))
                .build();
    }

    public static Role validRole() {
        return Role.builder()
                .roleId(1L)
                .name(RoleEnum.ROLE_USER)
                .users(null)
                .build();
    }
}
