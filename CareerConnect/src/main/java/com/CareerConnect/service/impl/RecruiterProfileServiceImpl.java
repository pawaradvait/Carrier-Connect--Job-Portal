package com.CareerConnect.service.impl;

import com.CareerConnect.entity.RecruiterProfile;
import com.CareerConnect.entity.User;
import com.CareerConnect.repo.RecruiterProfileRepo;
import com.CareerConnect.repo.UserRepo;
import com.CareerConnect.service.RecruiterProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RecruiterProfileServiceImpl implements RecruiterProfileService {

    @Autowired
    private RecruiterProfileRepo recruiterProfileRepo;

    @Autowired
    private UserRepo userRepo;

    @Override
    public Optional<RecruiterProfile> getRecuriterProfilebyUser(int userId) {
        return recruiterProfileRepo.findById(userId);
    }

    @Override
    public RecruiterProfile saveRecruiterProfile(RecruiterProfile recruiterProfile) {
        return recruiterProfileRepo.save(recruiterProfile);
    }

    @Override
    public RecruiterProfile getcuurentRecuriter() {
        Authentication authentication =SecurityContextHolder.getContext().getAuthentication();
        if(!(authentication instanceof AnonymousAuthenticationToken)){
            String currentUsername = authentication.getName();
            User user = userRepo.findByEmail(currentUsername).orElseThrow(()-> new UsernameNotFoundException("user not found"));

           RecruiterProfile recruiterProfile =  recruiterProfileRepo.findById(user.getUserId()).orElseThrow(()-> new UsernameNotFoundException("user not found"));
            return recruiterProfile;

        }
        return null;
    }
}
