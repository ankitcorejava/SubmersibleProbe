package com.submersible.probe.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.submersible.probe.entity.Command;

public interface CommandRepository extends JpaRepository<Command, Long> {
    List<Command> findByProbeIdOrderByTimestampAsc(Long probeId);
}

