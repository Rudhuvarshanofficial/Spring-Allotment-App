
package com.tnea.counselling.controller;

import com.tnea.counselling.dto.CollegeRequest;
import com.tnea.counselling.dto.CollegeResponse;
import com.tnea.counselling.dto.CourseResponse;
import com.tnea.counselling.service.CollegeService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class CollegeController {

    private final CollegeService collegeService;

    public CollegeController(CollegeService collegeService) {
        this.collegeService = collegeService;
    }

    @PostMapping("/admin/colleges")
    public ResponseEntity<CollegeResponse> registerCollege(
            @Valid @RequestBody CollegeRequest request) {

        CollegeResponse response = collegeService.registerCollege(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/colleges")
    public ResponseEntity<List<CollegeResponse>> getAllColleges() {

        return ResponseEntity.ok(collegeService.getAllColleges());
    }

    @GetMapping("/colleges/{collegeId}/courses")
    public ResponseEntity<List<CourseResponse>> getCoursesByCollegeId(
            @PathVariable Integer collegeId) {

        return ResponseEntity.ok(
                collegeService.getCoursesByCollegeId(collegeId)
        );
    }
}
