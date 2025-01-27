package com.employees.employees.controller;

import com.employees.employees.dto.EmployeeDto;
import com.employees.employees.exception.UpdateFailedException;
import com.employees.employees.service.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {
    private static final Logger logger = LoggerFactory.getLogger(EmployeeController.class);
    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @Operation(
            summary = "Create a new employee",
            description = "Creates a new employee(id, name, isTeamLead, teamId) in the system."
    )
    @ApiResponse(
            responseCode = "201",
            description = "Create succeed."
    )
    @ApiResponse(
            responseCode = "400",
            description = "Create failed : Employee id already exists."
    )
    @ApiResponse(
            responseCode = "404",
            description = "Create failed : Team with idTeam does not exits."
    )
    @PostMapping
    public ResponseEntity<EmployeeDto> createEmployee(@Valid @RequestBody EmployeeDto employeeDto) {
        logger.info("Received request to create a new employee: {}", employeeDto);
        EmployeeDto saved = employeeService.create(employeeDto);
        logger.info("Employee created successfully with ID: {}", saved.getId());
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @Operation(
            summary = "Get all employees",
            description = "Get all employees from the database."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Get succeed."
    )
    @GetMapping
    public ResponseEntity<List<EmployeeDto>> getAll() {
        logger.info("Received request to fetch all employees.");
        List<EmployeeDto> employees = employeeService.getAll();
        logger.info("Fetched {} employees from the database.", employees.size());
        return new ResponseEntity<>(employees, HttpStatus.OK);
    }

    @Operation(
            summary = "Get employee by id",
            description = "Get employee by id from the database."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Get succeed."
    )
    @ApiResponse(
            responseCode = "404",
            description = "Get failed - Employee with this id does not exists."
    )
    @GetMapping("/{id}")
    public ResponseEntity<EmployeeDto> getById(
            @Parameter(description = "Employee id", required = true)
            @PathVariable("id") Long id) {
        logger.info("Received request to fetch employee with ID: {}", id);
        EmployeeDto employeeDto = employeeService.getById(id);
        logger.info("Fetched employee: {}", employeeDto);
        return new ResponseEntity<>(employeeDto, HttpStatus.OK);
    }

    @Operation(
            summary = "Update employee",
            description = "Update particular employee in the database."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Update succeed."
    )
    @ApiResponse(
            responseCode = "400",
            description = "Updated failed - Team name already exists."
    )
    @ApiResponse(
            responseCode = "404",
            description = "Updated failed - Employee id does not exists."
    )
    @PutMapping("/{id}")
    public ResponseEntity<EmployeeDto> updateEmployee(@PathVariable("id") Long id, @Valid @RequestBody EmployeeDto employeeDto) {
        logger.info("Received request to update employee with ID: {}. Payload: {}", id, employeeDto);
        if (employeeDto.getId() != null && !id.equals(employeeDto.getId())) {
            logger.error("ID in URL ({}) does not match ID in request body ({}).", id, employeeDto.getId());
            throw new UpdateFailedException("ID in URL does not match ID in the request body");
        }
        EmployeeDto updated = employeeService.update(id, employeeDto);
        logger.info("Employee updated successfully with ID: {}", updated.getId());
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    @Operation(
            summary = "Delete employee",
            description = "Delete particular employee from the database."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Delete succeed."
    )
    @ApiResponse(
            responseCode = "404",
            description = "Delete failed: Employee id does not exists."
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteEmployee(@PathVariable("id") Long id) {
        logger.info("Received request to delete employee with ID: {}", id);
        employeeService.delete(id);
        logger.info("Employee with ID: {} deleted successfully.", id);
        return new ResponseEntity<>("User successfully deleted", HttpStatus.OK);
    }

    @Operation(
            summary = "Search employees",
            description = "Search employees by multiple optional filters."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Search succeed."
    )
    @GetMapping("/search")
    public ResponseEntity<List<EmployeeDto>> search(
                                                @RequestParam(required = false) Long id,
                                                @RequestParam(required = false) String name,
                                                @RequestParam(required = false) Boolean isTeamLead,
                                                @RequestParam(required = false) Integer idTeam) {
        logger.info("Received search request with parameters: id={}, name={}, isTeamLead={}, idTeam={}", id, name, isTeamLead, idTeam);
        List<EmployeeDto> employees = employeeService.search(id, name, isTeamLead, idTeam);
        logger.info("Search completed. Found {} employees.", employees.size());
        return new ResponseEntity<>(employees , HttpStatus.OK);
    }
}