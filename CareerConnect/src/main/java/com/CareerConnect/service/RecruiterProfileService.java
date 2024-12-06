package com.CareerConnect.service;

import com.CareerConnect.entity.RecruiterProfile;

import java.util.Optional;

public interface RecruiterProfileService {

    Optional<RecruiterProfile> getRecuriterProfilebyUser(int userId);

    RecruiterProfile saveRecruiterProfile(RecruiterProfile recruiterProfile);
}
