package com.markcode.springcloud.kafka.app.models;

public record Command<T>(
        String type,
        Long id,
        T body
) { }
