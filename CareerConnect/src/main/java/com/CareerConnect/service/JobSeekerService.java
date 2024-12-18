package com.CareerConnect.service;

import com.CareerConnect.entity.JobSeekerProfile;

import java.util.Optional;

public interface JobSeekerService {

    Optional<JobSeekerProfile> getOne(int id);

    JobSeekerProfile saveJobSeekerProfile(JobSeekerProfile jobSeekerProfile);

    JobSeekerProfile curentJObseeker();
}
