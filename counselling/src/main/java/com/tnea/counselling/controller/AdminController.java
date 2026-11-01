
package com.tnea.counselling.controller;

import com.tnea.counselling.dto.StudentResponse;
import com.tnea.counselling.model.College;
import com.tnea.counselling.model.Student;
import com.tnea.counselling.service.AdminService;
import com.tnea.counselling.service.AllotmentService;
import com.tnea.counselling.service.DemoDataService;
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

    @Autowired
    private DemoDataService demoDataService;

    @GetMapping("/students")
    public ResponseEntity<List<StudentResponse>> getAllStudents() {
        List<StudentResponse> students = service.getAllStudents()
                .stream()
                .map(StudentResponse::from)
                .toList();

        return ResponseEntity.ok(students);
    }

    @PostMapping("/counselling-run")
    public ResponseEntity<?> runCounselling() {
        return ResponseEntity.ok(service.runCounselling());
    }

    @GetMapping("/allotment/ranklist")
    public ResponseEntity<?> rankList() {
        return new ResponseEntity<>(
                allotmentService.rankList(),
                HttpStatus.OK
        );
    }

    @PostMapping("/demo/reset")
    public ResponseEntity<String> resetDemoData() {
        return ResponseEntity.ok(
                demoDataService.resetDemoData()
        );
    }

    @PostMapping("/demo/generate")
    public ResponseEntity<String> generateDemoData() {
        return ResponseEntity.ok(
                demoDataService.generateDemoData()
        );
    }
}
