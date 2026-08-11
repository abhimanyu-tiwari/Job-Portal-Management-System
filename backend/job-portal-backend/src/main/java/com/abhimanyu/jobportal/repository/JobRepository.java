package com.abhimanyu.jobportal.repository;

import com.abhimanyu.jobportal.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobRepository extends JpaRepository<Job, Long> {

}
