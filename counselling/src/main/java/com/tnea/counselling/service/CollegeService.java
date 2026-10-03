package com.tnea.counselling.service;

import com.tnea.counselling.ErrorHandling.NoRecordException;
import com.tnea.counselling.ErrorHandling.RegistrationException;
import com.tnea.counselling.model.College;
import com.tnea.counselling.model.Course;
import com.tnea.counselling.repository.CollegeDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;

@Service
public class CollegeService {

    @Autowired
    private CollegeDao dao;

    public College registerCollege(College college) {
        try{
            List<Course> courses = college.getCourse();
            if(courses.isEmpty()) throw new NoRecordException(LocalTime.now(),"No course listed","College need a course");
            dao.save(college);
            for (Course c : college.getCourse()) {
                c.setCollege(college);
            }
        }catch (NoRecordException e) {
            throw e;
        }
        catch(Exception e){
            throw new RegistrationException(LocalTime.now(),e.getMessage(),"College registration failed");
        }
        return dao.save(college);
    }

    public List<College> getAllColleges() {
        List<College> colleges = dao.findAll();
        if(colleges.isEmpty()) throw new NoRecordException(LocalTime.now(),"No colleges registered yet","No records on college");
        return colleges;
    }
}
