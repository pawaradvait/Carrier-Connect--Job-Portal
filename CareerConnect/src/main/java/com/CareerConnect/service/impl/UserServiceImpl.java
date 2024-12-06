package com.CareerConnect.service.impl;

import com.CareerConnect.entity.JobSeekerProfile;
import com.CareerConnect.entity.RecruiterProfile;
import com.CareerConnect.entity.User;
import com.CareerConnect.entity.UserType;
import com.CareerConnect.repo.JobSeekerProfileRepo;
import com.CareerConnect.repo.RecruiterProfileRepo;
import com.CareerConnect.repo.UserRepo;
import com.CareerConnect.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;
    private final JobSeekerProfileRepo jobSeekerProfileRepo;
    private final RecruiterProfileRepo  recruiterProfileRepo;
    private final PasswordEncoder passwordEncoder;
    @Override
    public User saveUser(User user) {
        user.setActive(true);
        user.setRegistrationDate(new java.util.Date(System.currentTimeMillis()));

      user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser =userRepo.save(user);

        UserType userType =savedUser.getUserTypeId();
         if(userType.getUserTypeId() == 2){
             JobSeekerProfile jobSeekerProfile = new JobSeekerProfile(savedUser);
             jobSeekerProfileRepo.save(jobSeekerProfile);
         }
         if(userType.getUserTypeId() == 1){
             recruiterProfileRepo.save(new RecruiterProfile(savedUser));
         }
        return userRepo.save(user);
    }

    @Override
    public Optional<User> getUser(String email) {

        return userRepo.findByEmail(email);
    }

    @Override
    public Object getCurrentUserProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String email = authentication.getName();
            User user = userRepo.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found"));
            if (user.getUserTypeId().getUserTypeId() == 1) {
                return recruiterProfileRepo.findById(user.getUserId()).orElseThrow(() -> new UsernameNotFoundException("User not found"));
            }
            if (user.getUserTypeId().getUserTypeId() == 2) {
                return jobSeekerProfileRepo.findById(user.getUserId()).orElseThrow(() -> new UsernameNotFoundException("User not found"));
            }


        }
        return null;
    }



}
