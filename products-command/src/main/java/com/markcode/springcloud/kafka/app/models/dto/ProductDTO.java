package com.markcode.springcloud.kafka.app.models.dto;

public record ProductDTO(
        Long id,
        String name,
        Double price
) { }
