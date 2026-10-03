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
@RequestMapping("student")
public class StudentController {

    @Autowired
    private StudentService service;

    @Autowired
    private PreferenceService preferenceService;


    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody Student student){
          return new ResponseEntity<>(service.register(student),HttpStatus.OK);
    }

    @GetMapping("/myDetails")
    public ResponseEntity<?> getMyDetails(Principal principal){
        return service.getMyDetails(principal);
    }


}
