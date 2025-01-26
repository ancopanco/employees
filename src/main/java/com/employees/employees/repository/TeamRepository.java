package com.employees.employees.repository;

import com.employees.employees.entity.Team;

import java.util.List;
import java.util.Optional;

public interface TeamRepository {
    Team create(String name);
    List<Team> getAll();
    Optional<Team> getById(Integer id);
    Optional<Team> getByName(String name);
    Team update(Integer id, Team teamDto);
    void delete(Integer id);
    List<Team> search(Integer id, String name);
}