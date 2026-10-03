package com.tnea.counselling.repository;

import com.tnea.counselling.model.Preference;
import com.tnea.counselling.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PreferenceDao extends JpaRepository<Preference,Integer> {
    //Student findByStudentEmail(String name);
}
