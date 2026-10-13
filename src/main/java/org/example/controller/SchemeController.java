package org.example.controller;

import org.example.dto.SchemeSuggestion;
import org.example.service.SchemeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/schemes")
public class SchemeController {

    @Autowired
    private SchemeService schemeService;

    @GetMapping("/suggestions")
    public List<SchemeSuggestion> suggestions(Authentication authentication) {
        return schemeService.getSuggestions(authentication.getName());
    }
}