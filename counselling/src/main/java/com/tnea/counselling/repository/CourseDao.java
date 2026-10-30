
package com.tnea.counselling.repository;

import com.tnea.counselling.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseDao extends JpaRepository<Course, Integer> {

    @Query("SELECT c FROM Course c WHERE c.college.id = ?2 AND c.courseId = ?1")
    Course findByCourseAndCollegeId(int courseId, int collegeId);

    List<Course> findAllByCollege_IdOrderByCourseIdAsc(Integer collegeId);
}
