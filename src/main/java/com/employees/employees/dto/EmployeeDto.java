package com.employees.employees.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class EmployeeDto {
    @Schema(description = "Unique identifier of the Employee.", example = "123456")
    @NotNull(message = "Employee ID cannot be null")
    Long id;

    @Schema(description = "Name of the employee. Must be not blank, and cannot exceed 255 characters.", example = "Mirko")
    @NotBlank(message = "Employee name cannot be blank")
    @Size(max = 255, message = "Employee name cannot exceed 255 characters")
    String name;

    @Schema(description = "Determine is employee team lead of its team or not.", example = "true")
    Boolean isTeamLead;

    @Schema(description = "Id of the team that employee belong. Must be not null.", example = "1")
    @NotNull(message = "Team ID cannot be null")
    Integer idTeam;
}