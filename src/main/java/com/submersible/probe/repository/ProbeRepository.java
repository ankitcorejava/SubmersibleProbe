package com.submersible.probe.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.submersible.probe.entity.Probe;

public interface ProbeRepository extends JpaRepository<Probe, Long> {
	List<Probe> findByGridId(Long gridId);
}
