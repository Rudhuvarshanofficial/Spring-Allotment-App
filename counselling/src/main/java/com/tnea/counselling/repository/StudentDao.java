
package com.tnea.counselling.repository;

import com.tnea.counselling.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentDao extends JpaRepository<Student, Integer> {

    Student findByStudentEmail(String studentEmail);

    List<Student> findAllByOrderByCutoffDesc();
}
