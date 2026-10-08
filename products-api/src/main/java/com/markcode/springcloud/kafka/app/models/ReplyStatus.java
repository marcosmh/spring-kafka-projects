package com.markcode.springcloud.kafka.app.models;

public enum ReplyStatus {
    SUCCESS,
    ERROR,
    WARN;

    public boolean isSuccess() {
        return this == SUCCESS;
    }
}
