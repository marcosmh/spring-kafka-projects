package com.markcode.springcloud.kafka.app.models;

public record Reply<T>(
        ReplyStatus status,
        String message,
        T body
) { }
