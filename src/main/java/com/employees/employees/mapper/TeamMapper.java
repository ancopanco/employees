package com.employees.employees.mapper;

import com.employees.employees.dto.TeamDto;
import com.employees.employees.entity.Team;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TeamMapper {
    TeamMapper MAPPER = Mappers.getMapper(TeamMapper.class);

    TeamDto mapToTeamDto(Team team);
    Team mapToTeam(TeamDto teamDto);
}