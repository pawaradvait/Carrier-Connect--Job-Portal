package com.CareerConnect.repo;

import com.CareerConnect.entity.JobSeekerProfile;
import com.CareerConnect.entity.JobSeekerSave;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobSeekerSaveRepo extends JpaRepository<JobSeekerSave, Integer> {
    List<JobSeekerSave> findByUserId(JobSeekerProfile jobSeekerProfile);
}
