package com.example.todo.rest.advice;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class ErrorMessage {

    private List<String> errors;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private LocalDateTime timestamp;

    public ErrorMessage() {
    }

    public ErrorMessage(List<String> errors) {
        this.errors = errors;
        timestamp = LocalDateTime.now();
    }

    public ErrorMessage(String error) {
        this(Collections.singletonList(error));
        timestamp = LocalDateTime.now();
    }

    public ErrorMessage(String ... errors) {
        this(Arrays.asList(errors));
        timestamp = LocalDateTime.now();
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
