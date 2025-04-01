package org.t1academy.tasktracker.exception.response;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public class ErrorResponse {
    private String errorMessage;
    @JsonFormat(
            pattern = "yyyy-MM-dd hh:mm:SSS"
    )
    private LocalDateTime timestamp;

    public static ErrorResponseBuilder builder() {
        return new ErrorResponseBuilder();
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }

    public LocalDateTime getTimestamp() {
        return this.timestamp;
    }

    public void setErrorMessage(final String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @JsonFormat(
            pattern = "yyyy-MM-dd hh:mm:SSS"
    )
    public void setTimestamp(final LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public ErrorResponse() {
    }

    public ErrorResponse(final String errorMessage, final LocalDateTime timestamp) {
        this.errorMessage = errorMessage;
        this.timestamp = timestamp;
    }

    public static class ErrorResponseBuilder {
        private String errorMessage;
        private LocalDateTime timestamp;

        ErrorResponseBuilder() {
        }

        public ErrorResponseBuilder errorMessage(final String errorMessage) {
            this.errorMessage = errorMessage;
            return this;
        }

        @JsonFormat(
                pattern = "yyyy-MM-dd hh:mm:SSS"
        )
        public ErrorResponseBuilder timestamp(final LocalDateTime timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public ErrorResponse build() {
            return new ErrorResponse(this.errorMessage, this.timestamp);
        }

        public String toString() {
            return "ErrorResponse.ErrorResponseBuilder(errorMessage=" + this.errorMessage + ", timestamp=" + this.timestamp + ")";
        }
    }
}
