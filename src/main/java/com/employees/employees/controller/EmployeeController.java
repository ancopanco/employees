package com.employees.employees.controller;

import com.employees.employees.dto.EmployeeDto;
import com.employees.employees.exception.UpdateFailedException;
import com.employees.employees.service.EmployeeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {
    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping
    public ResponseEntity<EmployeeDto> createEmployee(@Valid @RequestBody EmployeeDto employeeDto) {
        EmployeeDto saved = employeeService.create(employeeDto);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<EmployeeDto>> getAll() {
        List<EmployeeDto> employees = employeeService.getAll();
        return new ResponseEntity<>(employees, HttpStatus.OK);
    }
    @PutMapping("/{id}")
    public ResponseEntity<EmployeeDto> updateEmployee(@PathVariable("id") Long id, @Valid @RequestBody EmployeeDto employeeDto) {
        if (employeeDto.getId() != null && !id.equals(employeeDto.getId())) {
            throw new UpdateFailedException("ID in URL does not match ID in the request body");
        }
        EmployeeDto updated = employeeService.update(id, employeeDto);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteEmployee(@PathVariable("id") Long id) {
        employeeService.delete(id);
        return new ResponseEntity<>("User successfully deleted", HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<List<EmployeeDto>> search(
                                                @RequestParam(required = false) Long id,
                                                @RequestParam(required = false) String name,
                                                @RequestParam(required = false) Boolean isTeamLead,
                                                @RequestParam(required = false) Integer idTeam) {
        List<EmployeeDto> employees = employeeService.search(id, name, isTeamLead, idTeam);
        return new ResponseEntity<>(employees , HttpStatus.OK);
    }
}