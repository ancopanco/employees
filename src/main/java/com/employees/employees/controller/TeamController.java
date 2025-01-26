package com.employees.employees.controller;

import com.employees.employees.dto.TeamDto;
import com.employees.employees.exception.UpdateFailedException;
import com.employees.employees.service.TeamService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/teams")
public class TeamController {
    private final TeamService teamService;

    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }

    @Operation(
            summary = "Create a new team",
            description = "Creates a new team(id, name) in the system."
    )
    @ApiResponse(
            responseCode = "201",
            description = "Create succeed."
    )
    @ApiResponse(
            responseCode = "400",
            description = "Create failed : Team name already exists."
    )
    @PostMapping
    public ResponseEntity<TeamDto> create(@Valid @RequestBody TeamDto teamDto) {
        TeamDto saved = teamService.create(teamDto.getName());
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @Operation(
            summary = "Get all teams",
            description = "Get all teams from the database."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Get succeed."
    )
    @GetMapping()
    public ResponseEntity<List<TeamDto>> getAll() {
        List<TeamDto> teams = teamService.getAll();
        return new ResponseEntity<>(teams, HttpStatus.OK);
    }

    @Operation(
            summary = "Get team by id",
            description = "Get team by id from the database."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Get succeed."
    )
    @ApiResponse(
            responseCode = "404",
            description = "Get failed - Employee with this id does not exists."
    )
    @GetMapping("/{id}")
    public ResponseEntity<TeamDto> getById(
            @Parameter(description = "Team id", required = true)
            @PathVariable("id") Integer id) {
        TeamDto team = teamService.getById(id);
        return new ResponseEntity<>(team, HttpStatus.OK);
    }
    @Operation(
            summary = "Update team",
            description = "Update particular team in the database."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Update succeed."
    )
    @ApiResponse(
            responseCode = "400",
            description = "Updated failed - Team name already exists."
    )
    @ApiResponse(
            responseCode = "404",
            description = "Updated failed - Team id does not exists ."
    )
    @PutMapping("/{id}")
    public ResponseEntity<TeamDto> update(
            @Parameter(description = "Id of team that is going to be updated.", required = true)
            @PathVariable("id") Integer id,
            @Valid @RequestBody TeamDto teamDto) {

        if (teamDto.getId() != null && !id.equals(teamDto.getId())) {
            throw new UpdateFailedException("ID in URL does not match ID in the request body");
        }
        TeamDto updated = teamService.update(id, teamDto);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    @Operation(
            summary = "Delete team",
            description = "Delete particular team from the database."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Delete succeed."
    )
    @ApiResponse(
            responseCode = "404",
            description = "Delete failed : Team id does not exists."
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(
            @Parameter(description = "Id of team that is going to be deleted.", required = true)
            @PathVariable("id") Integer id) {
        teamService.delete(id);
        return new ResponseEntity<>("User successfully deleted", HttpStatus.OK);
    }

    @Operation(
            summary = "Search teams",
            description = "Search teams by multiple optional filters."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Search succeed."
    )
    @GetMapping("/search")
    public ResponseEntity<List<TeamDto>> search(
            @Parameter(description = "Team id.")
            @RequestParam(required = false) Integer id,
            @Parameter(description = "Team name.")
            @RequestParam(required = false) String name) {
        List<TeamDto> teams = teamService.search(id, name);
        return new ResponseEntity<>(teams, HttpStatus.OK);
    }
}