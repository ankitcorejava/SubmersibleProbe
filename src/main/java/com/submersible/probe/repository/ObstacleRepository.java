package com.submersible.probe.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.submersible.probe.entity.Obstacle;

public interface ObstacleRepository extends JpaRepository<Obstacle, Long> {
	List<Obstacle> findByGridId(Long gridId);
}
