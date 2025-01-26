package com.employees.employees.entity;

import lombok.Data;

@Data
public class Employee {
    Long id;
    String name;
    Boolean isTeamLead;
    Integer idTeam;
}