package com.example.finalprojectspringboot.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class ShowRequestDto {

    @NotBlank(message = "Movie name is required")
    private String movieName;

    @NotNull(message = "Show time is required")
    private LocalDateTime showTime;

    @NotNull(message = "Total seats is required")
    @Min(value = 1, message = "Total seats must be at least 1")
    private Integer totalSeats;

    public ShowRequestDto() {}

    public ShowRequestDto(String movieName, LocalDateTime showTime, Integer totalSeats) {
        this.movieName = movieName;
        this.showTime = showTime;
        this.totalSeats = totalSeats;
    }

    public String getMovieName() { return movieName; }
    public void setMovieName(String movieName) { this.movieName = movieName; }

    public LocalDateTime getShowTime() { return showTime; }
    public void setShowTime(LocalDateTime showTime) { this.showTime = showTime; }

    public Integer getTotalSeats() { return totalSeats; }
    public void setTotalSeats(Integer totalSeats) { this.totalSeats = totalSeats; }

    public static ShowRequestDtoBuilder builder() {
        return new ShowRequestDtoBuilder();
    }

    public static class ShowRequestDtoBuilder {
        private String movieName;
        private LocalDateTime showTime;
        private Integer totalSeats;

        public ShowRequestDtoBuilder movieName(String movieName) { this.movieName = movieName; return this; }
        public ShowRequestDtoBuilder showTime(LocalDateTime showTime) { this.showTime = showTime; return this; }
        public ShowRequestDtoBuilder totalSeats(Integer totalSeats) { this.totalSeats = totalSeats; return this; }

        public ShowRequestDto build() {
            return new ShowRequestDto(movieName, showTime, totalSeats);
        }
    }
}

