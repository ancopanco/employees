package com.employees.employees.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class EmployeeDto {
    @NotNull(message = "Employee ID cannot be null")
    Long id;

    @NotBlank(message = "Employee name cannot be blank")
    @Size(max = 255, message = "Employee name cannot exceed 255 characters")
    String name;

    Boolean isTeamLead;

    @NotNull(message = "Team ID cannot be null")
    Integer idTeam;
}