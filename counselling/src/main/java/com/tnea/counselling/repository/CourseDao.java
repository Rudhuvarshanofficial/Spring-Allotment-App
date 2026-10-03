package com.tnea.counselling.repository;

import com.tnea.counselling.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseDao extends JpaRepository<Course,Integer> {

    @Query(value = "select q from Course q where q.college.id = ?2 and q.courseId = ?1")
    Course findByCourseAndCollegeId(int courseId,int collegeId);
}
