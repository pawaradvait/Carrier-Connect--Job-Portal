package com.CareerConnect.service;

import com.CareerConnect.entity.JobPostActivity;
import com.CareerConnect.entity.JobSeekerApply;
import com.CareerConnect.entity.JobSeekerProfile;
import com.CareerConnect.repo.JobSeekerApplyRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobSeekerApplyService {

    @Autowired
    private  JobSeekerApplyRepo jobSeekerApplyRepo;

    public List<JobSeekerApply> getJObByCandidate(JobSeekerProfile jobSeekerProfile){

        return jobSeekerApplyRepo.findByUserId(jobSeekerProfile);
    }

    public List<JobSeekerApply>  getcandiadteforjobbasedonJObPost(JobPostActivity jobPostActivity){
        return jobSeekerApplyRepo.findbyJob(jobPostActivity);
    }



}
