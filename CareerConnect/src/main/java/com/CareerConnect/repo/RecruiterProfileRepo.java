package com.CareerConnect.repo;

import com.CareerConnect.entity.RecruiterProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecruiterProfileRepo extends JpaRepository<RecruiterProfile, Integer> {
}
