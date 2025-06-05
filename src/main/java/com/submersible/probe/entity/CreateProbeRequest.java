package com.submersible.probe.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateProbeRequest {
    private String name;
    private int x;
    private int y;
    private Direction direction;
}

