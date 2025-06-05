package com.submersible.probe.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.submersible.probe.entity.Command;
import com.submersible.probe.entity.CommandType;
import com.submersible.probe.entity.Direction;
import com.submersible.probe.entity.Grid;
import com.submersible.probe.entity.Obstacle;
import com.submersible.probe.entity.Probe;
import com.submersible.probe.entity.VisitedCoordinate;
import com.submersible.probe.repository.CommandRepository;
import com.submersible.probe.repository.GridRepository;
import com.submersible.probe.repository.ObstacleRepository;
import com.submersible.probe.repository.ProbeRepository;
import com.submersible.probe.repository.VisitedCoordinateRepository;

@ExtendWith(MockitoExtension.class)
class ProbeServiceTest {

    @InjectMocks
    private ProbeService probeService;

    @Mock
    private ProbeRepository probeRepository;
    @Mock
    private GridRepository gridRepository;
    @Mock
    private ObstacleRepository obstacleRepository;
    @Mock
    private VisitedCoordinateRepository visitedCoordinateRepository;
    @Mock
    private CommandRepository commandRepository;

    private Grid grid;
    private Probe probe;

    @BeforeEach
    void init() {
        grid = new Grid(1L, 5, 5, "Test Grid");
        probe = new Probe(1L, "TestProbe", 2, 2, Direction.NORTH, grid);

        lenient().when(commandRepository.save(any())).thenReturn(new Command());
        lenient().when(visitedCoordinateRepository.save(any())).thenReturn(new VisitedCoordinate());
        lenient().when(probeRepository.save(any())).thenAnswer(i -> i.getArgument(0));
    }

    @Test
    void testForwardMovementWithoutObstacles() {
        when(probeRepository.findById(1L)).thenReturn(Optional.of(probe));
        when(obstacleRepository.findByGridId(1L)).thenReturn(Collections.emptyList());

        Probe result = probeService.moveProbe(1L, Arrays.asList(CommandType.FORWARD));

        assertNotNull(result);
        assertEquals(2, result.getX());
        assertEquals(3, result.getY());
    }

    @Test
    void testMovementBlockedByObstacle() {
        probe.setX(1);
        probe.setY(1);
        probe.setDirection(Direction.EAST);

        Obstacle obstacle = new Obstacle(1L, 2, 1, grid);
        when(probeRepository.findById(1L)).thenReturn(Optional.of(probe));
        when(obstacleRepository.findByGridId(1L)).thenReturn(Collections.singletonList(obstacle));

        Probe result = probeService.moveProbe(1L, Arrays.asList(CommandType.FORWARD));

        assertNotNull(result);
        assertEquals(1, result.getX());
        assertEquals(1, result.getY());
    }

    @Test
    void testTurnLeft() {
        probe.setX(0);
        probe.setY(0);
        probe.setDirection(Direction.NORTH);

        when(probeRepository.findById(1L)).thenReturn(Optional.of(probe));
        when(obstacleRepository.findByGridId(1L)).thenReturn(Collections.emptyList());

        Probe result = probeService.moveProbe(1L, Arrays.asList(CommandType.LEFT));

        assertNotNull(result);
        assertEquals(Direction.WEST, result.getDirection());
    }

    @Test
    void testBackwardMovement() {
        probe.setX(2);
        probe.setY(2);
        probe.setDirection(Direction.NORTH);

        when(probeRepository.findById(1L)).thenReturn(Optional.of(probe));
        when(obstacleRepository.findByGridId(1L)).thenReturn(Collections.emptyList());

        Probe result = probeService.moveProbe(1L, Arrays.asList(CommandType.BACKWARD));

        assertNotNull(result);
        assertEquals(2, result.getX());
        assertEquals(1, result.getY());
    }

    @Test
    void testOutOfBoundsIgnored() {
        probe.setX(0);
        probe.setY(0);
        probe.setDirection(Direction.SOUTH);

        when(probeRepository.findById(1L)).thenReturn(Optional.of(probe));
        when(obstacleRepository.findByGridId(1L)).thenReturn(Collections.emptyList());

        Probe result = probeService.moveProbe(1L, Arrays.asList(CommandType.FORWARD));

        assertNotNull(result);
        assertEquals(0, result.getX());
        assertEquals(0, result.getY());
    }

    @Test
    void testRightTurn() {
        probe.setX(1);
        probe.setY(1);
        probe.setDirection(Direction.NORTH);

        when(probeRepository.findById(1L)).thenReturn(Optional.of(probe));
        when(obstacleRepository.findByGridId(1L)).thenReturn(Collections.emptyList());

        Probe result = probeService.moveProbe(1L, Arrays.asList(CommandType.RIGHT));

        assertNotNull(result);
        assertEquals(Direction.EAST, result.getDirection());
    }
}
