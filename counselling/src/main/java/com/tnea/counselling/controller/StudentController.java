package com.tnea.counselling.controller;

import com.tnea.counselling.model.Preference;
import com.tnea.counselling.model.Student;
import com.tnea.counselling.model.StudentWrapper;
import com.tnea.counselling.service.PreferenceService;
import com.tnea.counselling.service.StudentService;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;


@RestController
@RequestMapping("/api/v1/students")
public class StudentController {

    @Autowired
    private StudentService service;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody Student student){
          return new ResponseEntity<>(service.register(student),HttpStatus.CREATED);
    }

    @GetMapping("/me")
    public ResponseEntity<?> getMyDetails(Principal principal){
        return new ResponseEntity<>( service.getMyDetails(principal),HttpStatus.OK);
    }
}
