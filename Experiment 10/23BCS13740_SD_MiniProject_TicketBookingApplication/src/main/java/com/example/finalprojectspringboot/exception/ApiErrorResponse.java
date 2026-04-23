package com.example.finalprojectspringboot.exception;

import java.time.LocalDateTime;

public class ApiErrorResponse {

    private final LocalDateTime timestamp;
    private final int status;
    private final String error;
    private final String message;
    private final String path;

    public ApiErrorResponse(LocalDateTime timestamp, int status, String error, String message, String path) {
        this.timestamp = timestamp;
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
    }

    public LocalDateTime getTimestamp() { return timestamp; }
    public int getStatus() { return status; }
    public String getError() { return error; }
    public String getMessage() { return message; }
    public String getPath() { return path; }

    public static ApiErrorResponseBuilder builder() {
        return new ApiErrorResponseBuilder();
    }

    public static class ApiErrorResponseBuilder {
        private LocalDateTime timestamp;
        private int status;
        private String error;
        private String message;
        private String path;

        public ApiErrorResponseBuilder timestamp(LocalDateTime timestamp) { this.timestamp = timestamp; return this; }
        public ApiErrorResponseBuilder status(int status) { this.status = status; return this; }
        public ApiErrorResponseBuilder error(String error) { this.error = error; return this; }
        public ApiErrorResponseBuilder message(String message) { this.message = message; return this; }
        public ApiErrorResponseBuilder path(String path) { this.path = path; return this; }

        public ApiErrorResponse build() {
            return new ApiErrorResponse(timestamp, status, error, message, path);
        }
    }
}
