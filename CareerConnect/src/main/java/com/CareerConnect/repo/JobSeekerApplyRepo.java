package com.CareerConnect.repo;

import com.CareerConnect.entity.JobPostActivity;
import com.CareerConnect.entity.JobSeekerApply;
import com.CareerConnect.entity.JobSeekerProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobSeekerApplyRepo extends JpaRepository<JobSeekerApply, Integer> {

    List<JobSeekerApply> findByUserId(JobSeekerProfile userId);

    List<JobSeekerApply> findByJob(JobPostActivity jobPostActivity);
}
