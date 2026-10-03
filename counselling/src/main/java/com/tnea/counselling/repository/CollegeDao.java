package com.tnea.counselling.repository;

import com.tnea.counselling.model.College;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CollegeDao extends JpaRepository<College,Integer> {
    College findByCollegeName(String collegeName);
}
