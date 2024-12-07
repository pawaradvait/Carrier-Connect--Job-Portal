package com.CareerConnect.service.impl;

import com.CareerConnect.entity.JobSeekerProfile;
import com.CareerConnect.repo.JobSeekerProfileRepo;
import com.CareerConnect.service.JobSeekerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.Optional;

@Service
public class JobSeekerServiceImpl implements JobSeekerService {

    @Autowired
    private JobSeekerProfileRepo jobSeekerProfileRepo;

    @Override
    public Optional<JobSeekerProfile> getOne(int id) {

        Optional<JobSeekerProfile> jobSeekerProfile = jobSeekerProfileRepo.findById(id);

        if (jobSeekerProfile.isPresent()) {
            return jobSeekerProfile;
        } else {
            return null;
        }

    }

    @Override
    public JobSeekerProfile saveJobSeekerProfile(JobSeekerProfile jobSeekerProfile) {
        return jobSeekerProfileRepo.save(jobSeekerProfile);
    }
}
