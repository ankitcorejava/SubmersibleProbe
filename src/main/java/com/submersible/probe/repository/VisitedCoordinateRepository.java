package com.submersible.probe.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.submersible.probe.entity.VisitedCoordinate;

public interface VisitedCoordinateRepository extends JpaRepository<VisitedCoordinate, Long> {
	List<VisitedCoordinate> findByProbeIdOrderByTimestampAsc(Long probeId);
}
