package com.CareerConnect.repo;

import com.CareerConnect.entity.JobSeekerProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobSeekerProfileRepo extends JpaRepository<JobSeekerProfile, Integer> {
}
