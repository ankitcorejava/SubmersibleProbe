package com.submersible.probe.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.submersible.probe.entity.CommandType;
import com.submersible.probe.entity.Grid;
import com.submersible.probe.entity.Probe;
import com.submersible.probe.entity.VisitedCoordinate;
import com.submersible.probe.repository.GridRepository;
import com.submersible.probe.repository.ProbeRepository;
import com.submersible.probe.repository.VisitedCoordinateRepository;
import com.submersible.probe.service.ProbeService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/probes")
@RequiredArgsConstructor
public class ProbeController {

    private final ProbeRepository probeRepository;
    private final GridRepository gridRepository;
    private final ProbeService probeService;
    private final VisitedCoordinateRepository visitedCoordinateRepository;

    @PostMapping
    public ResponseEntity<Probe> createProbe(@RequestParam Long gridId,
                                             @RequestBody CreateProbeRequest request) {
        Grid grid = gridRepository.findById(gridId)
                .orElseThrow(() -> new IllegalArgumentException("Grid not found"));

        Probe probe = new Probe();
        probe.setName(request.getName());
        probe.setX(request.getX());
        probe.setY(request.getY());
        probe.setDirection(request.getDirection());
        probe.setGrid(grid);

        return ResponseEntity.ok(probeRepository.save(probe));
    }

    @PostMapping("/{id}/move")
    public ResponseEntity<Probe> moveProbe(@PathVariable Long id,
                                           @RequestBody List<CommandType> commands) {
        return ResponseEntity.ok(probeService.moveProbe(id, commands));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Probe> getProbe(@PathVariable Long id) {
        return ResponseEntity.of(probeRepository.findById(id));
    }

    @GetMapping("/{id}/summary")
    public ResponseEntity<List<VisitedCoordinate>> getSummary(@PathVariable Long id) {
        return ResponseEntity.ok(
                visitedCoordinateRepository.findByProbeIdOrderByTimestampAsc(id)
        );
    }
}

