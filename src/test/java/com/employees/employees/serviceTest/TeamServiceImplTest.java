package com.employees.employees.serviceTest;

import com.employees.employees.dto.TeamDto;
import com.employees.employees.entity.Team;
import com.employees.employees.repository.TeamRepository;
import com.employees.employees.service.impl.TeamServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

public class TeamServiceImplTest {
    @Mock
    private TeamRepository teamRepository;

    @InjectMocks
    private TeamServiceImpl teamService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetById() {
        Team mockTeam = new Team();
        mockTeam.setId(1);
        mockTeam.setName("Development");
        Mockito.when(teamRepository.getById(1)).thenReturn(Optional.of(mockTeam));

        TeamDto result = teamService.getById(1);

        Assertions.assertNotNull(result);
        Assertions.assertEquals("Development", result.getName());
        Mockito.verify(teamRepository, Mockito.times(1)).getById(1);
    }

}
