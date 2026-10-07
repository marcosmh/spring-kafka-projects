package com.markcode.springcloud.kafka.app.models.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ProductDTO(
        @NotBlank
        String name,

        @NotNull
        @Min(10)
        Double price
) { }
