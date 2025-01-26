package com.employees.employees.repository;

import com.employees.employees.dto.TeamDto;
import com.employees.employees.entity.Team;

import java.util.List;
import java.util.Optional;

public interface TeamRepository {
    TeamDto create(String name);
    List<TeamDto> getAll();
    Optional<Team> getTeamById(Integer id);
    Optional<Team> getTeamByName(String name);
    TeamDto update(Integer id, TeamDto teamDto);
    void delete(Integer id);
    List<TeamDto> search(Integer id, String name);
}