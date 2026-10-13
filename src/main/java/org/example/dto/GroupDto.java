package org.example.dto;

import lombok.Data;
import java.util.List;

@Data
public class GroupDto {
    private Long id;
    private String name;
    private List<String> members;
}
