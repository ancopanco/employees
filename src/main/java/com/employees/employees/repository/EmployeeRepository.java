package com.employees.employees.repository;

import com.employees.employees.dto.EmployeeDto;
import com.employees.employees.entity.Employee;

import java.util.List;

public interface EmployeeRepository {
    Employee create(Employee employee);
    List<Employee> getAll();
    Employee update(Long id, Employee employee);
    void delete(Long id);
    List<Employee> search(Long id, String name, Boolean isTeamLead, Integer idTeam);
}