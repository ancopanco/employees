package com.employees.employees.service.impl;

import com.employees.employees.dto.TeamDto;
import com.employees.employees.repository.TeamRepository;
import com.employees.employees.service.TeamService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeamServiceImpl implements TeamService {
    private final TeamRepository teamRepository;

    public TeamServiceImpl(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    @Override
    public TeamDto create(String name) {
        return teamRepository.create(name);
    }

    @Override
    public List<TeamDto> getAll() {
        return teamRepository.getAll();
    }

    @Override
    public TeamDto update(Integer id, TeamDto teamDto) {
        return teamRepository.update(id, teamDto);
    }

    @Override
    public void delete(Integer id) {
        teamRepository.delete(id);
    }

    @Override
    public List<TeamDto> search(Integer id, String name) {
        return teamRepository.search(id, name);
    }
}