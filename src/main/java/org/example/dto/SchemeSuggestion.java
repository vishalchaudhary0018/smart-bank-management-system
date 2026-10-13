package org.example.dto;

import lombok.Data;

@Data
public class SchemeSuggestion {
    private Long id;
    private String name;
    private String description;
    private String officialLink;
    private boolean newlyAdded;
}
