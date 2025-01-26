package com.employees.employees.service;

import com.employees.employees.dto.EmployeeDto;

import java.util.List;

public interface EmployeeService {
    EmployeeDto create(EmployeeDto employeeDto);
    List<EmployeeDto> getAll();
    EmployeeDto update(Long id, EmployeeDto employeeDto);
    void delete(Long id);
    List<EmployeeDto> search(Long id, String name, Boolean isTeamLead, Integer idTeam);
}