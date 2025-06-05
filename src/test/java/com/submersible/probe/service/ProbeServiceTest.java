package com.submersible.probe.service;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import com.submersible.probe.entity.CommandType;
import com.submersible.probe.entity.Direction;
import com.submersible.probe.entity.Grid;
import com.submersible.probe.entity.Obstacle;
import com.submersible.probe.entity.Probe;
import com.submersible.probe.repository.CommandRepository;
import com.submersible.probe.repository.GridRepository;
import com.submersible.probe.repository.ObstacleRepository;
import com.submersible.probe.repository.ProbeRepository;
import com.submersible.probe.repository.VisitedCoordinateRepository;


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

	@Test
	void testForwardMovementWithoutObstacles() {
		Grid grid = new Grid(1L, 5, 5, "Test Grid");
		Probe probe = new Probe(1L, "TestProbe", 2, 2, Direction.NORTH, grid);

		Mockito.when(probeRepository.findById(1L)).thenReturn(Optional.of(probe));
		Mockito.when(obstacleRepository.findByGridId(1L)).thenReturn(Collections.emptyList());
		Mockito.when(probeRepository.save(Mockito.any())).thenAnswer(i -> i.getArgument(0));

		Probe result = probeService.moveProbe(1L, Arrays.asList(CommandType.FORWARD));

		Assertions.assertEquals(2, result.getX());
		Assertions.assertEquals(3, result.getY());
	}

	@Test
	void testMovementBlockedByObstacle() {
		Grid grid = new Grid(1L, 5, 5, "Obstacle Grid");
		Probe probe = new Probe(1L, "BlockedProbe", 1, 1, Direction.EAST, grid);
		Obstacle obstacle = new Obstacle(1L, 2, 1, grid);

		Mockito.when(probeRepository.findById(1L)).thenReturn(Optional.of(probe));
		Mockito.when(obstacleRepository.findByGridId(1L)).thenReturn(Collections.singletonList(obstacle));

		Probe result = probeService.moveProbe(1L, Arrays.asList(CommandType.FORWARD));

		Assertions.assertEquals(1, result.getX());
		Assertions.assertEquals(1, result.getY()); // Should not move
	}

	@Test
	void testTurnLeft() {
		Grid grid = new Grid(1L, 5, 5, "Turn Grid");
		Probe probe = new Probe(1L, "TurningProbe", 0, 0, Direction.NORTH, grid);

		Mockito.when(probeRepository.findById(1L)).thenReturn(Optional.of(probe));
		Mockito.when(obstacleRepository.findByGridId(1L)).thenReturn(Collections.emptyList());

		Probe result = probeService.moveProbe(1L, Arrays.asList(CommandType.LEFT));

		Assertions.assertEquals(Direction.WEST, result.getDirection());
	}
}
