package com.employees.employees.dto;

import lombok.Data;

@Data
public class EmployeeDto {
    Long id;
    String name;
    Boolean isTeamLead;
    Integer idTeam;
}