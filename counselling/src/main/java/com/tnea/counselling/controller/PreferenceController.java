
package com.tnea.counselling.controller;

import com.tnea.counselling.dto.PreferenceRequest;
import com.tnea.counselling.dto.PreferenceResponse;
import com.tnea.counselling.service.PreferenceService;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/students/me/preferences")
public class PreferenceController {

    private final PreferenceService preferenceService;

    public PreferenceController(PreferenceService preferenceService) {
        this.preferenceService = preferenceService;
    }

    @PutMapping
    public ResponseEntity<List<PreferenceResponse>> setPreference(
            @Valid @RequestBody List<@Valid PreferenceRequest> preferences,
            Principal principal) {

        List<PreferenceResponse> savedPreferences =
                preferenceService.setPreference(preferences, principal);

        return ResponseEntity.ok(savedPreferences);
    }

    @GetMapping
    public ResponseEntity<List<PreferenceResponse>> getPreference(
            Principal principal) {

        return ResponseEntity.ok(
                preferenceService.getPreference(principal)
        );
    }
}
