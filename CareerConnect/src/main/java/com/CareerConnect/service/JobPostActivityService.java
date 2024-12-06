package com.CareerConnect.service;

import com.CareerConnect.entity.JobPostActivity;
import com.CareerConnect.entity.RecruiterJobDao;

import java.util.List;

public interface JobPostActivityService {

    JobPostActivity addNew(JobPostActivity jobPostActivity);

    List<RecruiterJobDao>  getRecuriterJobs(int recruiter);

    JobPostActivity getOne(int id);
}
