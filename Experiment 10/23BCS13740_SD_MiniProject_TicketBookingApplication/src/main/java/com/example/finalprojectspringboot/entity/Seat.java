package com.example.finalprojectspringboot.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import java.time.LocalDateTime;

@Entity
@Table(name = "seats")
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 20)
    private String seatNumber;

    @Column(nullable = false)
    private Long showId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private SeatStatus status;

    @Version
    @Column(nullable = false)
    private Integer version;

    private LocalDateTime lockedAt;

    public Seat() {}

    public Seat(Long id, String seatNumber, Long showId, SeatStatus status, Integer version, LocalDateTime lockedAt) {
        this.id = id;
        this.seatNumber = seatNumber;
        this.showId = showId;
        this.status = status;
        this.version = version;
        this.lockedAt = lockedAt;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getSeatNumber() { return seatNumber; }
    public void setSeatNumber(String seatNumber) { this.seatNumber = seatNumber; }

    public Long getShowId() { return showId; }
    public void setShowId(Long showId) { this.showId = showId; }

    public SeatStatus getStatus() { return status; }
    public void setStatus(SeatStatus status) { this.status = status; }

    public Integer getVersion() { return version; }
    public void setVersion(Integer version) { this.version = version; }

    public LocalDateTime getLockedAt() { return lockedAt; }
    public void setLockedAt(LocalDateTime lockedAt) { this.lockedAt = lockedAt; }

    public static SeatBuilder builder() {
        return new SeatBuilder();
    }

    public static class SeatBuilder {
        private Long id;
        private String seatNumber;
        private Long showId;
        private SeatStatus status;
        private Integer version;
        private LocalDateTime lockedAt;

        public SeatBuilder id(Long id) { this.id = id; return this; }
        public SeatBuilder seatNumber(String seatNumber) { this.seatNumber = seatNumber; return this; }
        public SeatBuilder showId(Long showId) { this.showId = showId; return this; }
        public SeatBuilder status(SeatStatus status) { this.status = status; return this; }
        public SeatBuilder version(Integer version) { this.version = version; return this; }
        public SeatBuilder lockedAt(LocalDateTime lockedAt) { this.lockedAt = lockedAt; return this; }

        public Seat build() {
            return new Seat(id, seatNumber, showId, status, version, lockedAt);
        }
    }
}
