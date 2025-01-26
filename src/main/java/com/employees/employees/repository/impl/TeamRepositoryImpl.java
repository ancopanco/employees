package com.employees.employees.repository.impl;

import com.employees.employees.dto.TeamDto;
import com.employees.employees.entity.Team;
import com.employees.employees.exception.*;
import com.employees.employees.mapper.TeamMapper;
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
import java.util.stream.Collectors;

@Repository
public class TeamRepositoryImpl implements TeamRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public TeamRepositoryImpl(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public TeamDto create(String name) {
        //check if team with given name already exists (name is unique)
        Optional<Team> teamDtoOptional = getTeamByName(name);
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

        TeamDto saved = new TeamDto();
        saved.setId(generatedKey);
        saved.setName(name);
        return saved;
    }

    @Override
    public List<TeamDto> getAll() {
        String sql = "SELECT * from Team WHERE isDeleted = FALSE";
        List<Team> teams = jdbcTemplate.query(sql, this::teamDtoMapper);
        return teams.stream().map(team -> TeamMapper.MAPPER.mapToTeamDto(team)).collect(Collectors.toList());
    }

    @Override
    public Optional<Team> getTeamById(Integer id) {
        String sql = "SELECT * FROM Team WHERE id = :id";
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
    public Optional<Team> getTeamByName(String name) {
        String sql = "SELECT * FROM Team WHERE name = :name";
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
    public TeamDto update(Integer id, TeamDto teamDto) {
        //find team by id
        Optional<Team> teamById = getTeamById(id);
        if (!teamById.isPresent()) {
            throw new RecordDoesNotExists("Team id does not exists");
        }
        //if try to update name with name which already exists
        Optional<Team> teamByName = getTeamByName(teamDto.getName());
        if (teamByName.isPresent() && !teamByName.get().getId().equals(id)) {
            throw new RecordAlreadyExistsException("Team name already exists");
        }

        String sql = "UPDATE Team SET name = :name WHERE id = :id";
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("name", teamDto.getName());
        parameters.addValue("id", id);
        int rowsAffected = jdbcTemplate.update(sql, parameters);
        if (rowsAffected == 0) {
            throw new UpdateFailedException("Update failed: no rows were affected");
        }
        teamDto.setId(id);
        return teamDto;
    }

    @Override
    public void delete(Integer id) {
        Optional<Team> teamOptional = getTeamById(id);
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
    public List<TeamDto> search(Integer id, String name) {
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
        List<Team> teams = jdbcTemplate.query(sql.toString(), parameters, this::teamDtoMapper);
        return teams.stream().map(team -> TeamMapper.MAPPER.mapToTeamDto(team)).collect(Collectors.toList());
    }

    private Team teamDtoMapper(ResultSet rs, int rowNum) throws SQLException {
        Team team = new Team();
        team.setId(rs.getInt("id"));
        team.setName(rs.getString("name"));
        team.setIsDeleted(rs.getBoolean("isDeleted"));
        return team;
    }
}