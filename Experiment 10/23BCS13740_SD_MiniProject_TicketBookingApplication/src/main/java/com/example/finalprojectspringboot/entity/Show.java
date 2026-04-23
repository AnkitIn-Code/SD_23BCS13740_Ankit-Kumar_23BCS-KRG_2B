package com.example.finalprojectspringboot.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "shows")
public class Show {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String movieName;

    @Column(nullable = false)
    private LocalDateTime showTime;

    @Column(nullable = false)
    private Integer totalSeats;

    @Column(nullable = false)
    private Integer availableSeats;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    public Show() {}

    public Show(Long id, String movieName, LocalDateTime showTime, Integer totalSeats, Integer availableSeats, LocalDateTime createdAt, LocalDateTime updatedAt) {
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

    public static ShowBuilder builder() {
        return new ShowBuilder();
    }

    public static class ShowBuilder {
        private Long id;
        private String movieName;
        private LocalDateTime showTime;
        private Integer totalSeats;
        private Integer availableSeats;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public ShowBuilder id(Long id) { this.id = id; return this; }
        public ShowBuilder movieName(String movieName) { this.movieName = movieName; return this; }
        public ShowBuilder showTime(LocalDateTime showTime) { this.showTime = showTime; return this; }
        public ShowBuilder totalSeats(Integer totalSeats) { this.totalSeats = totalSeats; return this; }
        public ShowBuilder availableSeats(Integer availableSeats) { this.availableSeats = availableSeats; return this; }
        public ShowBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public ShowBuilder updatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; return this; }

        public Show build() {
            return new Show(id, movieName, showTime, totalSeats, availableSeats, createdAt, updatedAt);
        }
    }
}


