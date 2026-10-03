package com.tnea.counselling.service;

import com.tnea.counselling.ErrorHandling.NoRecordException;
import com.tnea.counselling.model.Allotment;
import com.tnea.counselling.model.Student;
import com.tnea.counselling.repository.AllotmentDao;
import com.tnea.counselling.repository.StudentDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.*;

@Service
public class AllotmentService {

    @Autowired
    private AllotmentDao allotmentDao;

    @Autowired
    private StudentDao studentDao;

    public List<Allotment> rankList() {

        List<Student> students = studentDao.findAllByOrderByCutoffDesc();
        if(students.isEmpty()) throw new NoRecordException(LocalTime.now(),"Empty student list","No student records");
        List<Allotment> allotments = new ArrayList<>();
        for(Student s : students){
            allotments.add(new Allotment(s));
        }
        allotmentDao.saveAll(allotments);
        return allotments;
    }
}
