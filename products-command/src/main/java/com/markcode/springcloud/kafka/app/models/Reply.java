package com.markcode.springcloud.kafka.app.models;

public record Reply<T>(
        String status,
        String message,
        T body
) { }
