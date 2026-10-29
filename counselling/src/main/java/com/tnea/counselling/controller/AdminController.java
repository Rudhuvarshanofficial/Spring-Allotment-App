package com.tnea.counselling.controller;

import com.tnea.counselling.model.College;
import com.tnea.counselling.model.Student;
import com.tnea.counselling.service.AdminService;
import com.tnea.counselling.service.AllotmentService;
import com.tnea.counselling.service.CollegeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {

    @Autowired
    private AdminService service;

    @Autowired
    private AllotmentService allotmentService;

    @GetMapping("/students")
    public ResponseEntity<List<Student>> getAllStudents() {
        return ResponseEntity.ok(service.getAllStudents());
    }

    @PostMapping("/counselling-run")
    public ResponseEntity<?> runCounselling() {
        return ResponseEntity.ok(service.runCounselling());
    }

    @GetMapping("/allotment/ranklist")
    public ResponseEntity<?> rankList(){
        return new ResponseEntity<>(allotmentService.rankList(), HttpStatus.OK);
    }

}
