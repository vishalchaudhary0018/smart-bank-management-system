package org.example.controller;

import org.example.dto.ProfileDto;
import org.example.service.CustomerProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profile")
public class ProfileController {

    @Autowired
    private CustomerProfileService profileService;

    @PutMapping
    public ProfileDto save(@RequestBody ProfileDto dto, Authentication authentication) {
        return profileService.saveProfile(authentication.getName(), dto);
    }

    @GetMapping
    public ProfileDto get(Authentication authentication) {
        return profileService.getProfile(authentication.getName());
    }
}
