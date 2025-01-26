package com.employees.employees.entity;

import lombok.Data;

@Data
public class Employee {
    private Long id;
    private String name;
    private Boolean isTeamLead;
    private Integer idTeam;
}