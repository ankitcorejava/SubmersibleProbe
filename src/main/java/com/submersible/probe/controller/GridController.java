package com.submersible.probe.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.submersible.probe.entity.Grid;
import com.submersible.probe.repository.GridRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/grids")
@RequiredArgsConstructor
public class GridController {

	private final GridRepository gridRepository;

	@PostMapping
	public ResponseEntity<Grid> createGrid(@RequestBody Grid grid) {
		return ResponseEntity.ok(gridRepository.save(grid));
	}

	@GetMapping
	public ResponseEntity<List<Grid>> getAllGrids() {
		return ResponseEntity.ok(gridRepository.findAll());
	}
}
