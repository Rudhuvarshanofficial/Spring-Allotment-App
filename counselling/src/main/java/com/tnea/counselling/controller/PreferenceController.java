package com.tnea.counselling.controller;

import com.tnea.counselling.model.Preference;
import com.tnea.counselling.service.PreferenceService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/student")
public class PreferenceController {
    @Autowired
    private PreferenceService preferenceService;

    @PostMapping("/setPreference")
    public ResponseEntity<?> setPreference(@Valid @RequestBody List<Preference> preferences, Principal principal)throws Exception{
        return new ResponseEntity<>(preferenceService.setPreference(preferences,principal), HttpStatus.OK);
    }

    @GetMapping("/getPreference")
    public ResponseEntity<?> getPreference(Principal principal){
        return new ResponseEntity<>(preferenceService.getPreference(principal), HttpStatus.OK);
    }

}
