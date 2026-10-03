package com.tnea.counselling.service;

import com.tnea.counselling.ErrorHandling.NoRecordException;
import com.tnea.counselling.model.College;
import com.tnea.counselling.model.Course;
import com.tnea.counselling.model.Preference;
import com.tnea.counselling.model.Student;
import com.tnea.counselling.repository.CollegeDao;
import com.tnea.counselling.repository.CourseDao;
import com.tnea.counselling.repository.PreferenceDao;
import com.tnea.counselling.repository.StudentDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class PreferenceService {

    @Autowired
    private CollegeDao collegeDao;

    @Autowired
    private CourseDao courseDao;

    @Autowired
    private StudentDao studentDao;

    @Autowired
    private PreferenceDao prefernceDao;

    public List<Preference> setPreference(List<Preference> preferences, Principal principal) throws Exception {
         Student student =studentDao.findByStudentEmail(principal.getName());

         for(Preference p : preferences){
             //p.setStudentId(student.getId());
             College college =collegeDao.findById(p.getCollegeid()).orElseThrow(()-> new NoRecordException(LocalTime.now(),"College not registered yet","No college found"));
             Course course = courseDao.findByCourseAndCollegeId(p.getCourseid(),p.getCollegeid());
             if(course == null) throw new NoRecordException(LocalTime.now(),"College not registered yet","No college found");
             p.setCollege(college);
             p.setCourse(course);
             p.setStudent(student);
             prefernceDao.save(p);
         }
        student.setPreferences(preferences);
        return preferences;
    }

    public List<Preference> getPreference(Principal principal) {
        Student student = studentDao.findByStudentEmail(principal.getName());
        return student.getPreferences();
    }
}
