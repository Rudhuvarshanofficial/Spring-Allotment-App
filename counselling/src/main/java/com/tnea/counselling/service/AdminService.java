package com.tnea.counselling.service;

import com.tnea.counselling.ErrorHandling.NoRecordException;
import com.tnea.counselling.model.*;
import com.tnea.counselling.repository.AllotmentDao;
import com.tnea.counselling.repository.CollegeDao;
import com.tnea.counselling.repository.CourseDao;
import com.tnea.counselling.repository.StudentDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class AdminService {
    @Autowired
    private StudentDao studentDao;

    @Autowired
    private AllotmentDao allotmentDao;

    @Autowired
    private CourseDao courseDao;

    public List<Student> getAllStudents() {
        List<Student> students = studentDao.findAll();
        if(students.isEmpty()) throw new NoRecordException(LocalTime.now(),"Not registered yet" , "Empty student table");
        return students;
    }

    public String runCounselling() {
        List<Allotment> ranklist = allotmentDao.findAll();
        if(ranklist.isEmpty()) throw new NoRecordException(LocalTime.now(),"No details found","No Records on allotment");
        for(Allotment allocate : ranklist){
            if(!allocate.isAlloted()){
                allocateCollege(allocate);
            }
        }
        List<Allotment> unalloted = allotmentDao.findByisAllotedFalse();
        if(!unalloted.isEmpty()) {
            for (Allotment a : unalloted) {
                allocateCollegeGeneral(a);
            }
        }
        return "Success";
    }

    private void allocateCollegeGeneral(Allotment allocate) {
        Student student = studentDao.findById(allocate.getStudentId()).orElseThrow(() -> new NoRecordException(LocalTime.now(), "Not found", "No student Found"));
        List<Preference> preferences = student.getPreferences();
        if (!preferences.isEmpty()) {
            preferences.sort(Comparator.comparingInt(Preference::getPriority));
            for (Preference p : preferences) {
                int collegeId = p.getCollegeid();
                int courseId = p.getCourseid();
                Course course = courseDao.findByCourseAndCollegeId(courseId, collegeId);
                if (course == null) {
                    System.out.println(
                            "Invalid preference: College "
                                    + p.getCollegeid()
                                    + ", Course "
                                    + p.getCourseid()
                    );

                    continue;
                }

//                        System.out.println(
//                        "Student: " + student.getStudentName()
//                                + " | Priority: " + p.getPriority()
//                                + " | Preference College: " + collegeId
//                                + " | Preference Course: " + courseId
//                                + " | DB Course College: " + course.getCollege().getId()
//                                + " | DB Course Name: " + course.getCourseName()
//                                + " | Total Seats: " + course.getTotalSeats()
//                );
                if (course.getTotalSeats() > 0) {
                    List<String> communities = List.of("scSeats", "mbcSeats", "stSeats", "ocSeats", "bcSeats");
                    for (String community : communities) {
                        switch (community) {
                            case "scSeats": {
                                if (course.getScSeats() > 0) {
                                    course.setScSeats(course.getScSeats() - 1);
                                    allot(student, p, allocate, course);

                                    break;
                                }
                            }
                            ;
                            case "mbcSeats": {
                                if (course.getMbcSeats() > 0) {
                                    course.setMbcSeats(course.getMbcSeats() - 1);
                                    allot(student, p, allocate, course);

                                    break;
                                }
                            }
                            ;
                            case "stSeats": {
                                if (course.getStSeats() > 0) {
                                    course.setStSeats(course.getStSeats() - 1);
                                    allot(student, p, allocate, course);

                                    break;
                                }
                            }
                            ;
                            case "ocSeats": {
                                if (course.getOcSeats() > 0) {
                                    course.setOcSeats(course.getOcSeats() - 1);
                                    allot(student, p, allocate, course);

                                    break;
                                }
                            }
                            ;
                            case "bcSeats": {
                                if (course.getBcSeats() > 0) {
                                    course.setBcSeats(course.getBcSeats() - 1);
                                    allot(student, p, allocate, course);
                                    break;
                                }
                            }
                        }
                        if (allocate.isAlloted()) break;
                    }
                }
            }
    }
    }

    private void allot(Student student, Preference p,
                       Allotment allocate, Course course) {

        allocate.setAllotedCollege(p.getCollege().getCollegeName());
        allocate.setAllotedCourse(p.getCourse().getCourseName());
        allocate.setAlloted(true);

        course.setTotalSeats(course.getTotalSeats() - 1);

        courseDao.save(course);
        allotmentDao.save(allocate);
    }

    private void allocateCollege(Allotment allocate) {
        Student student = studentDao.findById(allocate.getStudentId()).orElseThrow(() -> new NoRecordException(LocalTime.now(), "Not found", "No student Found"));
        List<Preference> preferences = student.getPreferences();
        if (!preferences.isEmpty()) {
            preferences.sort(
                    Comparator.comparingInt(Preference::getPriority)
            );
            String studentCommunity = allocate.getStudentCommunity().toLowerCase() + "Seats";
            for (Preference p : preferences) {
                int collegeId = p.getCollegeid();
                int courseId = p.getCourseid();
                Course course = courseDao.findByCourseAndCollegeId(courseId, collegeId);
                if (course == null) {
                    System.out.println(
                            "Invalid preference: College "
                                    + p.getCollegeid()
                                    + ", Course "
                                    + p.getCourseid()
                    );
                    continue;
                }
                int seats = switch (studentCommunity) {
                        case "ocSeats" -> course.getOcSeats();
                        case "mbcSeats" -> course.getMbcSeats();
                        case "scSeats" -> course.getScSeats();
                        case "stSeats" -> course.getStSeats();
                        case "bcSeats" -> course.getBcSeats();
                        default -> 0;
                    };
                    if (seats > 0) {
                        switch (studentCommunity) {
                            case "ocSeats" -> course.setOcSeats(course.getOcSeats() - 1);
                            case "mbcSeats" -> course.setMbcSeats(course.getMbcSeats() - 1);
                            case "scSeats" -> course.setScSeats(course.getScSeats() - 1);
                            case "stSeats" -> course.setStSeats(course.getStSeats() - 1);
                            case "bcSeats" -> course.setBcSeats(course.getBcSeats() - 1);
                        }
                        allocate.setAllotedCollege(p.getCollege().getCollegeName());
                        allocate.setAllotedCourse(p.getCourse().getCourseName());
                        allocate.setAlloted(true);
                        course.setTotalSeats(course.getTotalSeats() - 1);
                        courseDao.save(course);
                        allotmentDao.save(allocate);
                    }
                if (allocate.isAlloted()) break;
            }
        }
    }
}
