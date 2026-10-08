package com.markcode.springcloud.kafka.app.models;

public record Command<T>(
        CommandType type,
        Long id,
        T body
) { }
