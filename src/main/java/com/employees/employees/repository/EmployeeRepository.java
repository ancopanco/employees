package com.employees.employees.repository;

import com.employees.employees.entity.Employee;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository {
    Employee create(Employee employee);
    List<Employee> getAll();
    Optional<Employee> getEmployeeById(Long id);
    Employee update(Long id, Employee employee);
    void delete(Long id);
    List<Employee> search(Long id, String name, Boolean isTeamLead, Integer idTeam);
}