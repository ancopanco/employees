package com.employees.employees.service.impl;

import com.employees.employees.dto.TeamDto;
import com.employees.employees.entity.Team;
import com.employees.employees.mapper.TeamMapper;
import com.employees.employees.repository.TeamRepository;
import com.employees.employees.service.TeamService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TeamServiceImpl implements TeamService {
    private final TeamRepository teamRepository;

    public TeamServiceImpl(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    @Override
    public TeamDto create(String name) {
        return TeamMapper.MAPPER.mapToTeamDto(teamRepository.create(name));
    }

    @Override
    public List<TeamDto> getAll() {
        List<Team> teams =teamRepository.getAll();
        return teams.stream().map(team -> TeamMapper.MAPPER.mapToTeamDto(team)).collect(Collectors.toList());
    }

    @Override
    public TeamDto update(Integer id, TeamDto teamDto) {
        Team team = TeamMapper.MAPPER.mapToTeam(teamDto);
        Team updated = teamRepository.update(id, team);
        return TeamMapper.MAPPER.mapToTeamDto(updated);
    }

    @Override
    public void delete(Integer id) {
        teamRepository.delete(id);
    }

    @Override
    public List<TeamDto> search(Integer id, String name) {
        List<Team> teams = teamRepository.search(id, name);
        return teams.stream().map(team -> TeamMapper.MAPPER.mapToTeamDto(team)).collect(Collectors.toList());
    }
}