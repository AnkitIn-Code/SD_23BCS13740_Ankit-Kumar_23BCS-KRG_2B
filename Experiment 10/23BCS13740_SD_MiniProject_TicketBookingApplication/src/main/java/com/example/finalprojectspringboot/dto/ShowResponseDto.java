package com.example.finalprojectspringboot.dto;

import java.time.LocalDateTime;

public class ShowResponseDto {

    private Long id;
    private String movieName;
    private LocalDateTime showTime;
    private Integer totalSeats;
    private Integer availableSeats;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public ShowResponseDto() {}

    public ShowResponseDto(Long id, String movieName, LocalDateTime showTime, Integer totalSeats, Integer availableSeats, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.movieName = movieName;
        this.showTime = showTime;
        this.totalSeats = totalSeats;
        this.availableSeats = availableSeats;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getMovieName() { return movieName; }
    public void setMovieName(String movieName) { this.movieName = movieName; }

    public LocalDateTime getShowTime() { return showTime; }
    public void setShowTime(LocalDateTime showTime) { this.showTime = showTime; }

    public Integer getTotalSeats() { return totalSeats; }
    public void setTotalSeats(Integer totalSeats) { this.totalSeats = totalSeats; }

    public Integer getAvailableSeats() { return availableSeats; }
    public void setAvailableSeats(Integer availableSeats) { this.availableSeats = availableSeats; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public static ShowResponseDtoBuilder builder() {
        return new ShowResponseDtoBuilder();
    }

    public static class ShowResponseDtoBuilder {
        private Long id;
        private String movieName;
        private LocalDateTime showTime;
        private Integer totalSeats;
        private Integer availableSeats;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public ShowResponseDtoBuilder id(Long id) { this.id = id; return this; }
        public ShowResponseDtoBuilder movieName(String movieName) { this.movieName = movieName; return this; }
        public ShowResponseDtoBuilder showTime(LocalDateTime showTime) { this.showTime = showTime; return this; }
        public ShowResponseDtoBuilder totalSeats(Integer totalSeats) { this.totalSeats = totalSeats; return this; }
        public ShowResponseDtoBuilder availableSeats(Integer availableSeats) { this.availableSeats = availableSeats; return this; }
        public ShowResponseDtoBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public ShowResponseDtoBuilder updatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; return this; }

        public ShowResponseDto build() {
            return new ShowResponseDto(id, movieName, showTime, totalSeats, availableSeats, createdAt, updatedAt);
        }
    }
}

