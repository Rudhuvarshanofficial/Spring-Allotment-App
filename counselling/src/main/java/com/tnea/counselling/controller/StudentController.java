
package com.tnea.counselling.controller;

import com.tnea.counselling.dto.StudentRegistrationRequest;
import com.tnea.counselling.dto.StudentResponse;
import com.tnea.counselling.service.StudentService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/students")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping("/register")
    public ResponseEntity<StudentResponse> register(
            @Valid @RequestBody StudentRegistrationRequest request) {

        var registeredStudent = studentService.register(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(StudentResponse.from(registeredStudent));
    }

    @GetMapping("/me")
    public ResponseEntity<StudentResponse> getMyDetails() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null
                || !authentication.isAuthenticated()
                || authentication instanceof AnonymousAuthenticationToken) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        var student = studentService.getMyDetails(
                () -> authentication.getName()
        );

        return ResponseEntity.ok(StudentResponse.from(student));
    }
}
