package com.employees.employees.service;

import com.employees.employees.dto.TeamDto;

import java.util.List;

public interface TeamService {
    TeamDto create(String name);
    List<TeamDto> getAll();
    TeamDto getById(Integer id);
    TeamDto update(Integer id, TeamDto teamDto);
    void delete(Integer id);
    List<TeamDto> search(Integer id, String name);
}