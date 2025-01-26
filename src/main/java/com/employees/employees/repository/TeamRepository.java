package com.employees.employees.repository;

import com.employees.employees.dto.TeamDto;

import java.util.List;

public interface TeamRepository {
    TeamDto create(String name);
    List<TeamDto> getAll();
    TeamDto update(Integer id, TeamDto teamDto);
    void delete(Integer id);
    List<TeamDto> search(Integer id, String name);
}