package com.employees.employees.repository.impl;

import com.employees.employees.dto.EmployeeDto;
import com.employees.employees.entity.Employee;
import com.employees.employees.exception.*;
import com.employees.employees.mapper.EmployeeMapper;
import com.employees.employees.repository.EmployeeRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class EmployeeRepositoryImpl implements EmployeeRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public EmployeeRepositoryImpl(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public EmployeeDto create(EmployeeDto employeeDto) {
        Optional<EmployeeDto> employeeDtoOptional = getEmployeeById(employeeDto.getId());
        if (employeeDtoOptional.isPresent()) {
            throw new RecordAlreadyExistsException("Employee id already exists");
        }

        String sql = "INSERT INTO Employee (id, name, isTeamLead, idTeam) VALUES (:id, :name, :isTeamLead, :idTeam)";
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("id", employeeDto.getId());
        parameters.addValue("name", employeeDto.getName());
        parameters.addValue("isTeamLead", employeeDto.getIsTeamLead());
        parameters.addValue("idTeam", employeeDto.getIdTeam());

        int rowsAffected = jdbcTemplate.update(sql, parameters);
        if (rowsAffected == 0) {
            throw new CreateFailedException("Create failed: no rows were affected");
        }
        return employeeDto;
    }

    @Override
    public List<EmployeeDto> getAll() {
        String sql = "SELECT * from Employee";
        List<Employee> employees = jdbcTemplate.query(sql, this::employeeMapper);
        return employees.stream().map(employee -> EmployeeMapper.MAPPER.mapToEmployeeDto(employee)).collect(Collectors.toList());
    }
    public Optional<EmployeeDto> getEmployeeById(Long id) {
        String sql = "SELECT * FROM Employee WHERE id = :id";
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("id", id);
        try {
            //todo da li new BeanPropertyRowMapper<>(Employee.class)
            Employee employee = jdbcTemplate.queryForObject(sql, parameters, this::employeeMapper);
            return Optional.of(EmployeeMapper.MAPPER.mapToEmployeeDto(employee));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public Optional<EmployeeDto> getTeamByName(String name) {
        String sql = "SELECT * FROM Employee WHERE name = :name";
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("name", name);
        try {
            Employee employee = jdbcTemplate.queryForObject(sql, parameters, this::employeeMapper);
            return Optional.of(EmployeeMapper.MAPPER.mapToEmployeeDto(employee));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public EmployeeDto update(Long id, EmployeeDto employeeDto) {
        //find team by id
        Optional<EmployeeDto> employeeById = getEmployeeById(id);
        if (!employeeById.isPresent()) {
            throw new RecordDoesNotExists("Employee id does not exists");
        }
        String sql = "UPDATE Employee SET name = :name, isTeamLead = :isTeamLead, idTeam = :idTeam WHERE id = :id";
        MapSqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("name", employeeDto.getName())
                .addValue("isTeamLead", employeeDto.getIsTeamLead())
                .addValue("idTeam", employeeDto.getIdTeam())
                .addValue("id", id);

        int rowsAffected = jdbcTemplate.update(sql, parameters);
        if (rowsAffected == 0) {
            throw new UpdateFailedException("Update failed: no rows were affected");
        }

        return employeeDto;
    }

    @Override
    public void delete(Long id) {
        Optional<EmployeeDto> employeeDtoOptional = getEmployeeById(id);
        if (!employeeDtoOptional.isPresent()) {
            throw new RecordDoesNotExists(String.format("Employee with ID %s does not exists", id));
        }

        String sql = "DELETE FROM Employee WHERE id = :id";
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("id", id);

        int rowsAffected = jdbcTemplate.update(sql, parameters);
        if (rowsAffected == 0) {
            throw new DeleteFailedException(String.format("Failed to delete team with id ", id));
        }
    }

    @Override
    public List<EmployeeDto> search(Long id, String name, Boolean isTeamLead, Integer idTeam) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        StringBuilder sb = new StringBuilder();
        if (id != null) {
            if (sb.length() > 0)
                sb.append(" AND ");
            sb.append("id = :id");
            parameters.addValue("id", id);
        }
        if (name != null) {
            if (sb.length() > 0)
                sb.append(" AND ");
            sb.append("LOWER(name) LIKE LOWER(:name)");
            parameters.addValue("name", "%" + name.trim() + "%");
        }
        if (isTeamLead != null) {
            if (sb.length() > 0)
                sb.append(" AND ");
            sb.append("isTeamLead = :isTeamLead");
            parameters.addValue("isTeamLead", isTeamLead);
        }

        if (idTeam != null) {
            if (sb.length() > 0)
                sb.append(" AND ");
            sb.append("idTeam = :idTeam");
            parameters.addValue("idTeam", idTeam);
        }

        if (sb.length() > 0)
            sb.insert(0, "WHERE ");

        String where = sb.toString();

        String sql = String.format("SELECT * FROM Employee %s", where);
        List<Employee> employees = jdbcTemplate.query(sql.toString(), parameters, this::employeeMapper);
        return employees.stream().map(employee -> EmployeeMapper.MAPPER.mapToEmployeeDto(employee)).collect(Collectors.toList());
    }

    private Employee employeeMapper(ResultSet rs, int rowNum) throws SQLException {
        Employee employee = new Employee();
        employee.setId(rs.getLong("id"));
        employee.setName(rs.getString("name"));
        employee.setIsTeamLead(rs.getBoolean("isTeamLead"));
        employee.setIdTeam(rs.getInt("idTeam"));
        return employee;
    }
}