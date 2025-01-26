package com.employees.employees.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class TeamDto {
    private Integer id;

    @NotBlank(message = "Team name cannot be blank (empty or whitespace)")
    @Size(max = 50, message = "Team name cannot exceed 50 characters")
    private String name;
}
