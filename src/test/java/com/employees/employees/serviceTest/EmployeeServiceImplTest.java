package com.employees.employees.serviceTest;

import com.employees.employees.dto.EmployeeDto;
import com.employees.employees.entity.Employee;
import com.employees.employees.repository.EmployeeRepository;
import com.employees.employees.service.impl.EmployeeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class EmployeeServiceImplTest {
    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateEmployee() {
        Employee employee = new Employee();
        employee.setId(111111L);
        employee.setName("Ana");
        employee.setIdTeam(1);
        employee.setIsTeamLead(false);

        when(employeeRepository.create(any())).thenReturn(employee);

        EmployeeDto employeeDto = new EmployeeDto();
        employeeDto.setId(employee.getId());
        employeeDto.setName(employee.getName());
        employeeDto.setIdTeam(employee.getIdTeam());
        employeeDto.setIsTeamLead(employee.getIsTeamLead());

        EmployeeDto createdEmployee = employeeService.create(employeeDto);

        assertNotNull(createdEmployee);
        assertEquals(employee.getId(), createdEmployee.getId());
        verify(employeeRepository, times(1)).create(any());
    }
}