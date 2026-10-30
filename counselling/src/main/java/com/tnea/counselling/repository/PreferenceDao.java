
package com.tnea.counselling.repository;

import com.tnea.counselling.model.Preference;
import com.tnea.counselling.model.Student;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PreferenceDao
        extends JpaRepository<Preference, Integer> {

    List<Preference> findAllByStudentOrderByPriorityAsc(
            Student student
    );

    void deleteAllByStudent(Student student);
}
