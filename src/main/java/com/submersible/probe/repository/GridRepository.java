package com.submersible.probe.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.submersible.probe.entity.Grid;

public interface GridRepository extends JpaRepository<Grid, Long> {
}
