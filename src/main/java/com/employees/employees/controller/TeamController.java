package com.employees.employees.controller;

import com.employees.employees.dto.TeamDto;
import com.employees.employees.exception.UpdateFailedException;
import com.employees.employees.service.TeamService;
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

    @PostMapping
    public ResponseEntity<TeamDto> create(@Valid @RequestBody TeamDto teamDto) {
        TeamDto saved = teamService.create(teamDto.getName());
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<TeamDto>> getAll() {
        List<TeamDto> teams = teamService.getAll();
        return new ResponseEntity<>(teams, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TeamDto> update(@PathVariable("id") Integer id, @Valid @RequestBody TeamDto teamDto) {

        if (teamDto.getId() != null && !id.equals(teamDto.getId())) {
            throw new UpdateFailedException("ID in URL does not match ID in the request body");
        }
        TeamDto updated = teamService.update(id, teamDto);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable("id") Integer id) {
        teamService.delete(id);
        return new ResponseEntity<>("User successfully deleted", HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<List<TeamDto>> search(@RequestParam(required = false) Integer id,
                                                @RequestParam(required = false) String name) {
        List<TeamDto> teams = teamService.search(id, name);
        return new ResponseEntity<>(teams, HttpStatus.OK);
    }
}