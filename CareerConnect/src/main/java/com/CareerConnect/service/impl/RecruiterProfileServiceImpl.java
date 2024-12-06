package com.CareerConnect.service.impl;

import com.CareerConnect.entity.RecruiterProfile;
import com.CareerConnect.repo.RecruiterProfileRepo;
import com.CareerConnect.service.RecruiterProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RecruiterProfileServiceImpl implements RecruiterProfileService {

    @Autowired
    private RecruiterProfileRepo recruiterProfileRepo;

    @Override
    public Optional<RecruiterProfile> getRecuriterProfilebyUser(int userId) {
        return recruiterProfileRepo.findById(userId);
    }

    @Override
    public RecruiterProfile saveRecruiterProfile(RecruiterProfile recruiterProfile) {
        return recruiterProfileRepo.save(recruiterProfile);
    }
}
