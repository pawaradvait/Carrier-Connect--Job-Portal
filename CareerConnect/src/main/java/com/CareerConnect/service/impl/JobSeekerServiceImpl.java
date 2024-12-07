package com.CareerConnect.service.impl;

import com.CareerConnect.entity.JobSeekerProfile;
import com.CareerConnect.entity.User;
import com.CareerConnect.repo.JobSeekerProfileRepo;
import com.CareerConnect.repo.UserRepo;
import com.CareerConnect.service.JobSeekerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.Optional;

@Service
public class JobSeekerServiceImpl implements JobSeekerService {

    @Autowired
    private JobSeekerProfileRepo jobSeekerProfileRepo;
    @Autowired
    private UserRepo userRepo;

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

    @Override
    public JobSeekerProfile curentJObseeker() {

        Authentication authentication =SecurityContextHolder.getContext().getAuthentication();
        if(!(authentication instanceof AnonymousAuthenticationToken)){

            User user =userRepo.findByEmail(authentication.getName()).orElseThrow(()-> new UsernameNotFoundException("user not found"));
            JobSeekerProfile jobSeekerProfile =jobSeekerProfileRepo.findById(user.getUserId()).orElseThrow(()-> new UsernameNotFoundException("user not found"));
            return jobSeekerProfile;
        }
        return null;
    }
}
