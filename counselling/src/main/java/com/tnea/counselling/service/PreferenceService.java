
package com.tnea.counselling.service;

import com.tnea.counselling.ErrorHandling.NoRecordException;
import com.tnea.counselling.dto.PreferenceRequest;
import com.tnea.counselling.dto.PreferenceResponse;
import com.tnea.counselling.model.College;
import com.tnea.counselling.model.Course;
import com.tnea.counselling.model.Preference;
import com.tnea.counselling.model.Student;
import com.tnea.counselling.repository.CollegeDao;
import com.tnea.counselling.repository.CourseDao;
import com.tnea.counselling.repository.PreferenceDao;
import com.tnea.counselling.repository.StudentDao;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class PreferenceService {

    private final CollegeDao collegeDao;
    private final CourseDao courseDao;
    private final StudentDao studentDao;
    private final PreferenceDao preferenceDao;

    public PreferenceService(
            CollegeDao collegeDao,
            CourseDao courseDao,
            StudentDao studentDao,
            PreferenceDao preferenceDao) {

        this.collegeDao = collegeDao;
        this.courseDao = courseDao;
        this.studentDao = studentDao;
        this.preferenceDao = preferenceDao;
    }

    @Transactional
    public List<PreferenceResponse> setPreference(
            List<PreferenceRequest> requests,
            Principal principal) {

        Student student = getStudent(principal);

        List<Preference> newPreferences = new ArrayList<>();

        for (PreferenceRequest request : requests) {

            College college = collegeDao.findById(request.collegeid())
                    .orElseThrow(() -> new NoRecordException(
                            LocalTime.now(),
                            "College not found",
                            "No college exists with the supplied ID"
                    ));

            Course course = courseDao.findByCourseAndCollegeId(
                    request.courseid(),
                    request.collegeid()
            );

            if (course == null) {
                throw new NoRecordException(
                        LocalTime.now(),
                        "Course not found",
                        "The course does not belong to the selected college"
                );
            }

            Preference preference = new Preference();
            preference.setPriority(request.priority());
            preference.setCollegeid(request.collegeid());
            preference.setCourseid(request.courseid());
            preference.setCollege(college);
            preference.setCourse(course);
            preference.setStudent(student);

            newPreferences.add(preference);
        }

        // Replace the existing list only after validating all entries.
        preferenceDao.deleteAllByStudent(student);

        List<Preference> savedPreferences =
                preferenceDao.saveAll(newPreferences);

        return savedPreferences.stream()
                .map(PreferenceResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<PreferenceResponse> getPreference(
            Principal principal) {

        Student student = getStudent(principal);

        return preferenceDao
                .findAllByStudentOrderByPriorityAsc(student)
                .stream()
                .map(PreferenceResponse::from)
                .toList();
    }

    private Student getStudent(Principal principal) {

        Student student =
                studentDao.findByStudentEmail(principal.getName());

        if (student == null) {
            throw new NoRecordException(
                    LocalTime.now(),
                    "Student not found",
                    "No student profile exists for the authenticated user"
            );
        }

        return student;
    }
}
