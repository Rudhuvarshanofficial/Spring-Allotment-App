
package com.tnea.counselling.controller;

import com.tnea.counselling.model.Preference;
import com.tnea.counselling.service.PreferenceService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/students/me/preferences")
public class PreferenceController {

    @Autowired
    private PreferenceService preferenceService;

    @PutMapping
    public ResponseEntity<?> setPreference( @Valid @RequestBody List<Preference> preferences, Principal principal) throws Exception {
        return ResponseEntity.ok(preferenceService.setPreference(preferences, principal));
    }

    @GetMapping
    public ResponseEntity<?> getPreference(Principal principal) {
        return ResponseEntity.ok(preferenceService.getPreference(principal));
    }
}
