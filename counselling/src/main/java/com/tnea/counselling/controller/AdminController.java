package com.tnea.counselling.controller;

import com.tnea.counselling.model.College;
import com.tnea.counselling.model.Student;
import com.tnea.counselling.service.AdminService;
import com.tnea.counselling.service.CollegeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("admin")
public class AdminController {

    @Autowired
    private CollegeService collegeService;

    @Autowired
    private AdminService service;

    @GetMapping("/getStudents")
    public ResponseEntity<List<Student>> getAllStudents(){
        return new ResponseEntity<>(service.getAllStudents(), HttpStatus.OK);
    }

    @GetMapping("/runCounselling")
    public ResponseEntity<?> runCounselling(){
        return new ResponseEntity<>(service.runCounselling(),HttpStatus.OK);
    }

}
