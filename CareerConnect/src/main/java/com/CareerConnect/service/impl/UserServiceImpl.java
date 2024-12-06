package com.CareerConnect.service.impl;

import com.CareerConnect.entity.User;
import com.CareerConnect.repo.UserRepo;
import com.CareerConnect.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;
    @Override
    public User saveUser(User user) {
        user.setActive(true);
        user.setRegistrationDate(new java.util.Date(System.currentTimeMillis()));
        return userRepo.save(user);
    }

    @Override
    public Optional<User> getUser(String email) {

        return userRepo.findByEmail(email);
    }
}
