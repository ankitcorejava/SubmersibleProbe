package com.submersible.probe.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

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

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProbeService {

    private final ProbeRepository probeRepository;
    private final GridRepository gridRepository;
    private final ObstacleRepository obstacleRepository;
    private final VisitedCoordinateRepository visitedCoordinateRepository;
    private final CommandRepository commandRepository;

    public Probe moveProbe(Long probeId, List<CommandType> commands) {
        Probe probe = probeRepository.findById(probeId)
                .orElseThrow(() -> new IllegalArgumentException("Probe not found"));

        Grid grid = probe.getGrid();
        List<Obstacle> obstacles = obstacleRepository.findByGridId(grid.getId());

        for (CommandType command : commands) {
            // Save the command
            commandRepository.save(new Command(null, command, LocalDateTime.now(), probe));

            switch (command) {
                case LEFT:
                    probe.setDirection(turnLeft(probe.getDirection()));
                    break;
                case RIGHT:
                    probe.setDirection(turnRight(probe.getDirection()));
                    break;
                case FORWARD:
                case BACKWARD:
                    int[] deltas = moveDelta(probe.getDirection(), command == CommandType.BACKWARD);
                    int nextX = probe.getX() + deltas[0];
                    int nextY = probe.getY() + deltas[1];

                    // Check boundaries
                    if (nextX < 0 || nextY < 0 || nextX >= grid.getWidth() || nextY >= grid.getHeight()) {
                        continue; // Skip out-of-bounds
                    }

                    // Check for obstacle
                    boolean blocked = obstacles.stream().anyMatch(ob ->
                            ob.getX() == nextX && ob.getY() == nextY);
                    if (blocked) continue;

                    // Move probe
                    probe.setX(nextX);
                    probe.setY(nextY);

                    visitedCoordinateRepository.save(new VisitedCoordinate(
                            null, nextX, nextY, LocalDateTime.now(), probe
                    ));
                    break;
            }
        }

        return probeRepository.save(probe);
    }

    private Direction turnLeft(Direction dir) {
        switch (dir) {
            case NORTH: return Direction.WEST;
            case WEST:  return Direction.SOUTH;
            case SOUTH: return Direction.EAST;
            case EAST:  return Direction.NORTH;
        }
        return dir;
    }

    private Direction turnRight(Direction dir) {
        switch (dir) {
            case NORTH: return Direction.EAST;
            case EAST:  return Direction.SOUTH;
            case SOUTH: return Direction.WEST;
            case WEST:  return Direction.NORTH;
        }
        return dir;
    }

    private int[] moveDelta(Direction dir, boolean isBackward) {
        int dx = 0, dy = 0;
        switch (dir) {
            case NORTH: dy = 1; break;
            case SOUTH: dy = -1; break;
            case EAST:  dx = 1; break;
            case WEST:  dx = -1; break;
        }
        if (isBackward) {
            dx = -dx;
            dy = -dy;
        }
        return new int[]{dx, dy};
    }
}

