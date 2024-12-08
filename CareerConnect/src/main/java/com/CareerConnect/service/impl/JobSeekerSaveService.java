package com.CareerConnect.service.impl;

import com.CareerConnect.entity.JobSeekerProfile;
import com.CareerConnect.entity.JobSeekerSave;
import com.CareerConnect.entity.User;
import com.CareerConnect.repo.JobSeekerSaveRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobSeekerSaveService {

    @Autowired
    private JobSeekerSaveRepo jobSeekerSaveRepo;

    public List<JobSeekerSave> getJobSeekerSave(JobSeekerProfile jobSeekerProfile){
        return jobSeekerSaveRepo.findByUserId(jobSeekerProfile);
    }


    public void saveJobSeekerSave(JobSeekerSave jobSeekerSave) {

         jobSeekerSaveRepo.save(jobSeekerSave);
     }

    public List<JobSeekerSave> getSavedJobs(JobSeekerProfile jobSeekerProfile) {

        return jobSeekerSaveRepo.findByUserId(jobSeekerProfile);
    }
}
