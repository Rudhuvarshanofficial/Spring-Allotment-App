
package com.tnea.counselling.service;

import com.tnea.counselling.ErrorHandling.NoRecordException;
import com.tnea.counselling.dto.StudentRegistrationRequest;
import com.tnea.counselling.model.Student;
import com.tnea.counselling.model.Users;
import com.tnea.counselling.repository.StudentDao;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.time.LocalTime;

@Service
public class StudentService {

    private final UsersService usersService;
    private final StudentDao dao;
    private final BCryptPasswordEncoder encoder =
            new BCryptPasswordEncoder(12);

    public StudentService(
            UsersService usersService,
            StudentDao dao) {
        this.usersService = usersService;
        this.dao = dao;
    }

    @Transactional
    public Student register(StudentRegistrationRequest request) {

        String email = request.studentEmail().trim();

        String encodedPassword =
                encoder.encode(request.studentPassword());

        Student student = new Student();
        student.setStudentName(request.studentName().trim());
        student.setStudentEmail(email);
        student.setStudentPassword(encodedPassword);
        student.setMathsMark(request.mathsMark());
        student.setSecondMark(request.secondMark());
        student.setThirdMark(request.thirdMark());
        student.setStudentCommunity(request.studentCommunity());

        int cutoff = request.mathsMark()
                + (request.secondMark() + request.thirdMark()) / 2;

        student.setCutoff(cutoff);

        Users user = new Users(
                email,
                encodedPassword,
                "STUDENT"
        );

        usersService.register(user);

        return dao.save(student);
    }

    @Transactional(readOnly = true)
    public Student getMyDetails(Principal principal)
            throws UsernameNotFoundException {

        Student student =
                dao.findByStudentEmail(principal.getName());

        if (student == null) {
            throw new NoRecordException(
                    LocalTime.now(),
                    "Not registered yet",
                    "Username not found"
            );
        }

        return student;
    }
}
