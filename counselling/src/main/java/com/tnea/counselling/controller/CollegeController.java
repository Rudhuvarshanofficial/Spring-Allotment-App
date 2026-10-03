package com.tnea.counselling.controller;

import com.tnea.counselling.model.College;
import com.tnea.counselling.service.CollegeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("admin")
public class CollegeController {
    @Autowired
    private CollegeService collegeService;
    @PostMapping("/registerCollege")
    public ResponseEntity<?> registerCollege(@Valid @RequestBody College college){
        return new ResponseEntity<>(collegeService.registerCollege(college), HttpStatus.OK);
    }

    @GetMapping("college")
    public ResponseEntity<?> getAllColleges(){
        return new ResponseEntity<>(collegeService.getAllColleges(),HttpStatus.OK);
    }
}
