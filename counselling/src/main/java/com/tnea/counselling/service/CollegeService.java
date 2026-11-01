
package com.tnea.counselling.service;

import com.tnea.counselling.ErrorHandling.NoRecordException;
import com.tnea.counselling.dto.CollegeRequest;
import com.tnea.counselling.dto.CollegeResponse;
import com.tnea.counselling.dto.CourseRequest;
import com.tnea.counselling.dto.CourseResponse;
import com.tnea.counselling.model.College;
import com.tnea.counselling.model.Course;
import com.tnea.counselling.repository.CollegeDao;
import com.tnea.counselling.repository.CourseDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.List;

@Service
public class CollegeService {

    private final CollegeDao collegeDao;
    private final CourseDao courseDao;
    private final SeatDistributionService seatDistributionService;

    public CollegeService(
            CollegeDao collegeDao,
            CourseDao courseDao,
            SeatDistributionService seatDistributionService) {

        this.collegeDao = collegeDao;
        this.courseDao = courseDao;
        this.seatDistributionService = seatDistributionService;
    }

    @Transactional
    public CollegeResponse registerCollege(CollegeRequest request) {

        College college = new College();

        college.setCollegeName(request.collegeName().trim());
        college.setCollegeLocation(request.collegeLocation().trim());
        college.setCollegeType(request.collegeType());

        List<Course> courses = request.course()
                .stream()
                .map(courseRequest -> mapToCourse(
                        courseRequest,
                        college
                ))
                .toList();

        college.setCourse(courses);

        College savedCollege = collegeDao.save(college);

        return CollegeResponse.from(savedCollege);
    }

    @Transactional(readOnly = true)
    public List<CollegeResponse> getAllColleges() {

        List<College> colleges = collegeDao.findAll();

        if (colleges.isEmpty()) {
            throw new NoRecordException(
                    LocalTime.now(),
                    "No colleges registered yet",
                    "No college records are available"
            );
        }

        return colleges.stream()
                .map(CollegeResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<CourseResponse> getCoursesByCollegeId(Integer collegeId) {

        collegeDao.findById(collegeId)
                .orElseThrow(() -> new NoRecordException(
                        LocalTime.now(),
                        "College not found",
                        "No college exists with ID: " + collegeId
                ));

        return courseDao.findAllByCollege_IdOrderByCourseIdAsc(collegeId)
                .stream()
                .map(CourseResponse::from)
                .toList();
    }

    private Course mapToCourse(
            CourseRequest request,
            College college) {

        Course course = new Course();

        course.setCourseId(request.courseId());
        course.setCourseName(request.courseName().trim());
        course.setTotalSeats(request.totalSeats());
        course.setCollege(college);

        seatDistributionService.distributeSeats(
                course,
                college.getCollegeType()
        );

        return course;
    }
}
