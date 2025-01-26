package com.employees.employees.repository.impl;

import com.employees.employees.entity.Team;
import com.employees.employees.exception.*;
import com.employees.employees.repository.TeamRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class TeamRepositoryImpl implements TeamRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public TeamRepositoryImpl(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Team create(String name) {
        Optional<Team> teamDtoOptional = getByName(name);
        if (teamDtoOptional.isPresent()) {
            throw new RecordAlreadyExistsException("Team name already exists");
        }

        String sql = "INSERT INTO Team (name) VALUES (:name)";
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("name", name);

        KeyHolder keyHolder = new GeneratedKeyHolder();

        int rowsAffected = jdbcTemplate.update(sql, parameters, keyHolder);
        if (rowsAffected == 0) {
            throw new CreateFailedException("Create failed: no rows were affected");
        }

        Integer generatedKey = keyHolder.getKey().intValue();

        Team saved = new Team();
        saved.setId(generatedKey);
        saved.setName(name);
        return saved;
    }

    @Override
    public List<Team> getAll() {
        String sql = "SELECT * from Team WHERE isDeleted = FALSE";
        return jdbcTemplate.query(sql, this::teamDtoMapper);
    }

    @Override
    public Optional<Team> getById(Integer id) {
        String sql = "SELECT * FROM Team WHERE isDeleted = FALSE AND id = :id";
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("id", id);
        try {
            Team team = jdbcTemplate.queryForObject(sql, parameters, this::teamDtoMapper);
            return Optional.of(team);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Team> getByName(String name) {
        String sql = "SELECT * FROM Team WHERE isDeleted = FALSE AND name = :name";
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("name", name);
        try {
            Team team = jdbcTemplate.queryForObject(sql, parameters, this::teamDtoMapper);
            return Optional.of(team);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Team update(Integer id, Team team) {
        //find team by id
        Optional<Team> teamById = getById(id);
        if (!teamById.isPresent()) {
            throw new RecordDoesNotExists("Team id does not exists");
        }
        //if try to update name with name which already exists
        Optional<Team> teamByName = getByName(team.getName());
        if (teamByName.isPresent() && !teamByName.get().getId().equals(id)) {
            throw new RecordAlreadyExistsException("Team name already exists");
        }

        String sql = "UPDATE Team SET name = :name WHERE id = :id";
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("name", team.getName());
        parameters.addValue("id", id);
        int rowsAffected = jdbcTemplate.update(sql, parameters);
        if (rowsAffected == 0) {
            throw new UpdateFailedException("Update failed: no rows were affected");
        }
        return getById(id)
                .orElseThrow(() -> new RecordDoesNotExists("Team id does not exists"));
    }

    @Override
    public void delete(Integer id) {
        Optional<Team> teamOptional = getById(id);
        if (!teamOptional.isPresent()) {
            throw new RecordDoesNotExists(String.format("Team with ID %s does not exists", id));
        }

        String sql = "UPDATE Team SET isDeleted = TRUE WHERE id = :id";
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("id", id);
        jdbcTemplate.update(sql, parameters);

        int rowsAffected = jdbcTemplate.update(sql, parameters);
        if (rowsAffected == 0) {
            throw new DeleteFailedException(String.format("Failed to delete team with id ", id));
        }
    }

    @Override
    public List<Team> search(Integer id, String name) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        StringBuilder sb = new StringBuilder("WHERE isDeleted = FALSE");

        if (id != null) {
            sb.append(" AND id = :id");
            parameters.addValue("id", id);
        }
        if (name != null) {
            sb.append(" AND LOWER(name) LIKE LOWER(:name)");
            parameters.addValue("name", "%" + name.trim() + "%");
        }

        String where = sb.toString();

        String sql = String.format("SELECT * FROM Team %s", where);
        return jdbcTemplate.query(sql.toString(), parameters, this::teamDtoMapper);
    }

    private Team teamDtoMapper(ResultSet rs, int rowNum) throws SQLException {
        Team team = new Team();
        team.setId(rs.getInt("id"));
        team.setName(rs.getString("name"));
        team.setIsDeleted(rs.getBoolean("isDeleted"));
        return team;
    }
}