package com.CareerConnect.service;

import com.CareerConnect.entity.User;

import java.util.Objects;
import java.util.Optional;

public interface UserService {

    User saveUser(User user);
    Optional<User> getUser(String email);

    Object getCurrentUserProfile();
}
