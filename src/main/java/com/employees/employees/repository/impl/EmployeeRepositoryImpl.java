package com.employees.employees.repository.impl;

import com.employees.employees.entity.Employee;
import com.employees.employees.entity.Team;
import com.employees.employees.exception.*;
import com.employees.employees.repository.EmployeeRepository;
import com.employees.employees.repository.TeamRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class EmployeeRepositoryImpl implements EmployeeRepository {
    private static final Logger logger = LoggerFactory.getLogger(EmployeeRepositoryImpl.class);
    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final TeamRepository teamRepository;

    public EmployeeRepositoryImpl(NamedParameterJdbcTemplate jdbcTemplate, TeamRepository teamRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.teamRepository = teamRepository;
    }

    @Override
    public Employee create(Employee employee) {
        Optional<Employee> employeeDtoOptional = getById(employee.getId());
        if (employeeDtoOptional.isPresent()) {
            logger.error("Employee creation failed: ID {} already exists", employee.getId());
            throw new RecordAlreadyExistsException(String.format("Employee ID %s already exists", employee.getId()));
        }

        Optional<Team> team = teamRepository.getById(employee.getIdTeam());

        if (!team.isPresent() || (team.isPresent() && team.get().getIsDeleted())) {
            logger.error("Team with ID {} does not exist", employee.getIdTeam());
            throw new RecordDoesNotExists(String.format("Team ID %s does not exist", employee.getIdTeam()));
        }

        String sql = "INSERT INTO Employee (id, name, isTeamLead, idTeam) VALUES (:id, :name, :isTeamLead, :idTeam)";
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("id", employee.getId());
        parameters.addValue("name", employee.getName());
        parameters.addValue("isTeamLead", employee.getIsTeamLead());
        parameters.addValue("idTeam", employee.getIdTeam());

        int rowsAffected = jdbcTemplate.update(sql, parameters);
        if (rowsAffected == 0) {
            logger.error("Employee creation failed: no rows were affected");
            throw new CreateFailedException("Create failed: no rows were affected");
        }
        return employee;
    }

    @Override
    public List<Employee> getAll() {
        String sql = "SELECT * from Employee";
        return jdbcTemplate.query(sql, this::employeeMapper);
    }
    @Override
    public Optional<Employee> getById(Long id) {
        String sql = "SELECT * FROM Employee WHERE id = :id";
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("id", id);
        try {
            Employee employee = jdbcTemplate.queryForObject(sql, parameters, this::employeeMapper);
            return Optional.of(employee);
        } catch (EmptyResultDataAccessException e) {
            logger.warn("No employee found with ID: {}", id);
            return Optional.empty();
        }
    }

    @Override
    public Employee update(Long id, Employee employee) {
        Optional<Employee> employeeByIdOptional = getById(id);
        if (!employeeByIdOptional.isPresent()) {
            logger.error("Update failed: Employee ID {} does not exist", id);
            throw new RecordDoesNotExists(String.format("Employee ID %s does not exists", id));
        }
        if (employee.getIdTeam() != null) {
            Optional<Team> team = teamRepository.getById(employee.getIdTeam());

            if (!team.isPresent() || (team.isPresent() && team.get().getIsDeleted())) {
                logger.error("Update failed: Team ID {} does not exist", employee.getIdTeam());
                throw new RecordDoesNotExists(String.format("Team ID %s does not exist", employee.getIdTeam()));
            }
        }

        Employee employeeById = employeeByIdOptional.get();
        String sql = "UPDATE Employee SET name = :name, isTeamLead = :isTeamLead, idTeam = :idTeam WHERE id = :id";
        MapSqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("name", employee.getName() == null ? employeeById.getName() : employee.getName())
                .addValue("isTeamLead", employee.getIsTeamLead() == null ? employeeById.getIsTeamLead() : employee.getIsTeamLead())
                .addValue("idTeam", employee.getIdTeam() == null ? employeeById.getIdTeam() : employee.getIdTeam())
                .addValue("id", id);

        int rowsAffected = jdbcTemplate.update(sql, parameters);
        if (rowsAffected == 0) {
            logger.error("Update failed: no rows were affected for employee ID {}", id);
            throw new UpdateFailedException(String.format("Failed to update employee with ID ", id));
        }

        return getById(id)
                .orElseThrow(() -> new RecordDoesNotExists(String.format("Employee ID %s already exists", id)));
    }

    @Override
    public void delete(Long id) {
        Optional<Employee> employeeDtoOptional = getById(id);
        if (!employeeDtoOptional.isPresent()) {
            logger.error("Delete failed: Employee ID {} does not exist", id);
            throw new RecordDoesNotExists(String.format("Employee with ID %s does not exists", id));
        }

        String sql = "DELETE FROM Employee WHERE id = :id";
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("id", id);

        int rowsAffected = jdbcTemplate.update(sql, parameters);
        if (rowsAffected == 0) {
            logger.error("Delete failed: no rows were affected for employee ID {}", id);
            throw new DeleteFailedException(String.format("Failed to delete team with ID ", id));
        }
    }

    @Override
    public List<Employee> search(Long id, String name, Boolean isTeamLead, Integer idTeam) {
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
        return jdbcTemplate.query(sql.toString(), parameters, this::employeeMapper);
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