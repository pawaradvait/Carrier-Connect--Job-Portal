package com.CareerConnect.service;

import com.CareerConnect.entity.JobPostActivity;
import com.CareerConnect.entity.RecruiterJobDao;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

public interface JobPostActivityService {

    JobPostActivity addNew(JobPostActivity jobPostActivity);

    List<RecruiterJobDao>  getRecuriterJobs(int recruiter);

    JobPostActivity getOne(int id);

    List<JobPostActivity> getAll();

    List<JobPostActivity> search(String job,String location ,List<String> remote ,List<String>
            jobType,LocalDate searchDate);

}
