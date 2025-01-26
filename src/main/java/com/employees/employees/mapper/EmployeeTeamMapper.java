package com.employees.employees.mapper;

import com.employees.employees.dto.EmployeeTeamDto;
import com.employees.employees.entity.Employee;
import com.employees.employees.entity.Team;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface EmployeeTeamMapper {
    EmployeeTeamMapper MAPPER = Mappers.getMapper(EmployeeTeamMapper.class);
    @Mapping(source = "employee.id", target = "id")
    @Mapping(source = "employee.name", target = "name")
    @Mapping(source = "employee.isTeamLead", target = "isTeamLead")
    @Mapping(source = "employee.idTeam", target = "idTeam")
    @Mapping(source = "team.name", target = "nameTeam")
    EmployeeTeamDto mapToEmployeeTeamDto(Employee employee, Team team);
}