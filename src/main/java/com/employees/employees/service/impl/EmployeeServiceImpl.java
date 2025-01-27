package com.employees.employees.service.impl;

import com.employees.employees.dto.EmployeeDto;
import com.employees.employees.entity.Employee;
import com.employees.employees.exception.RecordDoesNotExists;
import com.employees.employees.mapper.EmployeeMapper;
import com.employees.employees.repository.EmployeeRepository;
import com.employees.employees.service.EmployeeService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository employeeRepository;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public EmployeeDto create(EmployeeDto employeeDto) {
        Employee employee = EmployeeMapper.MAPPER.mapToEmployee(employeeDto);
        Employee saved = employeeRepository.create(employee);
        return EmployeeMapper.MAPPER.mapToEmployeeDto(saved);
    }

    @Override
    public List<EmployeeDto> getAll() {
        List<Employee> employees = employeeRepository.getAll();
        return employees.stream().map(employee -> EmployeeMapper.MAPPER.mapToEmployeeDto(employee)).collect(Collectors.toList());
    }

    @Override
    public EmployeeDto getById(Long id) {
        Optional<Employee> employee = employeeRepository.getById(id);
        if (!employee.isPresent()) {
            throw new RecordDoesNotExists(String.format("Team ID %s does not exist", id));
        }
        return EmployeeMapper.MAPPER.mapToEmployeeDto(employee.get());
    }

    @Override
    public EmployeeDto update(Long id, EmployeeDto employeeDto) {
        Employee employee = EmployeeMapper.MAPPER.mapToEmployee(employeeDto);
        Employee updated = employeeRepository.update(id, employee);
        return EmployeeMapper.MAPPER.mapToEmployeeDto(updated);
    }

    @Override
    public void delete(Long id) {
        employeeRepository.delete(id);
    }

    @Override
    public List<EmployeeDto> search(Long id, String name, Boolean isTeamLead, Integer idTeam) {
        List<Employee> employees = employeeRepository.search(id, name, isTeamLead, idTeam);
        return employees.stream().map(employee -> EmployeeMapper.MAPPER.mapToEmployeeDto(employee)).collect(Collectors.toList());
    }
}