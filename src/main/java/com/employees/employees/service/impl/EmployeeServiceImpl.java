package com.employees.employees.service.impl;

import com.employees.employees.dto.EmployeeDto;
import com.employees.employees.repository.EmployeeRepository;
import com.employees.employees.service.EmployeeService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository employeeRepository;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public EmployeeDto create(EmployeeDto employeeDto) {
        return employeeRepository.create(employeeDto);
    }

    @Override
    public List<EmployeeDto> getAll() {
        return employeeRepository.getAll();
    }

    @Override
    public EmployeeDto update(Long id, EmployeeDto employeeDto) {
        return employeeRepository.update(id, employeeDto);
    }

    @Override
    public void delete(Long id) {
        employeeRepository.delete(id);
    }

    @Override
    public List<EmployeeDto> search(Long id, String name, Boolean isTeamLead, Integer idTeam) {
        return employeeRepository.search(id, name, isTeamLead, idTeam);
    }
}