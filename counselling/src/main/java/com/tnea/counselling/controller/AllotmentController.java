package com.tnea.counselling.controller;

import com.tnea.counselling.model.Allotment;
import com.tnea.counselling.service.AllotmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("admin")
public class AllotmentController {

    @Autowired
    private AllotmentService allotmentService;

    @GetMapping("allotment/ranklist")
    public ResponseEntity<?> rankList(){
        return new ResponseEntity<>(allotmentService.rankList(), HttpStatus.OK);
    }
}
