package com.employees.employees.dto;

import lombok.Data;

@Data
public class EmployeeTeamDto {
    Long id;
    String name;
    Boolean isTeamLead;
    Integer idTeam;
    String nameTeam;
}