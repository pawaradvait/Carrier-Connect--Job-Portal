package com.CareerConnect.service.impl;

import com.CareerConnect.entity.*;
import com.CareerConnect.repo.JobPostActivityRepo;
import com.CareerConnect.service.JobPostActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class JobPostActivityServiceImpl implements JobPostActivityService {
    @Autowired
    private JobPostActivityRepo jobPostActivityRepository;

    @Override
    public JobPostActivity addNew(JobPostActivity jobPostActivity) {
        return jobPostActivityRepository.save(jobPostActivity);
    }

    @Override
    public List<RecruiterJobDao> getRecuriterJobs(int recruiter) {

        List<IRecruiterJobs> iRecruiterJobs = jobPostActivityRepository.getRecuriterJobs(recruiter);

        List<RecruiterJobDao> recruiterJobDaos = new ArrayList<>();

        for(IRecruiterJobs iRecruiterJobs1 : iRecruiterJobs){
            RecruiterJobDao recruiterJobDao = new RecruiterJobDao();
            recruiterJobDao.setTotalCandidates(iRecruiterJobs1.getTotalCandidates());
            recruiterJobDao.setJobPostId(iRecruiterJobs1.getJob_post_id());
            recruiterJobDao.setJobTitle(iRecruiterJobs1.getJob_title());
            recruiterJobDao.setJobLocationId(new JobLocation(iRecruiterJobs1.getLocationId(),iRecruiterJobs1.getCity(),iRecruiterJobs1.getState(),iRecruiterJobs1.getCountry()));
            recruiterJobDao.setJobCompanyId(new JobCompany(iRecruiterJobs1.getCompanyId(),iRecruiterJobs1.getName(),""));
            recruiterJobDaos.add(recruiterJobDao);
        }
        return recruiterJobDaos;


    }

    @Override
    public JobPostActivity getOne(int id) {
        Optional<JobPostActivity> jobPostActivity = jobPostActivityRepository.findById(id);
        if(jobPostActivity.isPresent()){
            return jobPostActivity.get();
        }else{
            return null;
        }
    }


}
