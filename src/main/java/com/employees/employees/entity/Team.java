package com.employees.employees.entity;

import lombok.Data;
import org.springframework.jdbc.core.RowMapper;

@Data
public class Team {
    private int id;
    private String name;
}
