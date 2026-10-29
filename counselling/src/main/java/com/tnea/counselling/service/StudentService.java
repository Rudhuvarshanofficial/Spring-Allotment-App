package com.tnea.counselling.service;

import com.tnea.counselling.ErrorHandling.NoRecordException;
import com.tnea.counselling.ErrorHandling.RegistrationException;
import com.tnea.counselling.model.Student;
import com.tnea.counselling.model.Users;
import com.tnea.counselling.repository.StudentDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalTime;

@Service
public class StudentService {

    @Autowired
    private UsersService usersService;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);



    @Autowired
    private StudentDao dao;

    public Student register(Student student){
        try {
            student.setCutoff(student.getMathsMark() + ((student.getSecondMark() + student.getThirdMark()) / 2));
            student.setStudentPassword(encoder.encode(student.getStudentPassword()));
            usersService.register(new Users(student.getStudentEmail(), student.getStudentPassword(), "STUDENT"));
            return dao.save(student);
        } catch (Exception e) {
            throw new RegistrationException(LocalTime.now(),e.getMessage(),"Registration failed! Give details properly");
        }
    }

    public Student getMyDetails(Principal principle) throws UsernameNotFoundException {
        Student student = dao.findByStudentEmail(principle.getName());
        if (student == null) throw new NoRecordException(LocalTime.now(),"Not registered yet","Username not found");
        return student;
    }
}
