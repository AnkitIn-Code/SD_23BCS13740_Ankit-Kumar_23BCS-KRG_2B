package com.example.finalprojectspringboot.dto;

import com.example.finalprojectspringboot.entity.SeatStatus;
import java.time.LocalDateTime;

public class SeatResponseDto {

    private Long id;
    private String seatNumber;
    private Long showId;
    private SeatStatus status;
    private Integer version;
    private LocalDateTime lockedAt;

    public SeatResponseDto() {}

    public SeatResponseDto(Long id, String seatNumber, Long showId, SeatStatus status, Integer version, LocalDateTime lockedAt) {
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

    public static SeatResponseDtoBuilder builder() {
        return new SeatResponseDtoBuilder();
    }

    public static class SeatResponseDtoBuilder {
        private Long id;
        private String seatNumber;
        private Long showId;
        private SeatStatus status;
        private Integer version;
        private LocalDateTime lockedAt;

        public SeatResponseDtoBuilder id(Long id) { this.id = id; return this; }
        public SeatResponseDtoBuilder seatNumber(String seatNumber) { this.seatNumber = seatNumber; return this; }
        public SeatResponseDtoBuilder showId(Long showId) { this.showId = showId; return this; }
        public SeatResponseDtoBuilder status(SeatStatus status) { this.status = status; return this; }
        public SeatResponseDtoBuilder version(Integer version) { this.version = version; return this; }
        public SeatResponseDtoBuilder lockedAt(LocalDateTime lockedAt) { this.lockedAt = lockedAt; return this; }

        public SeatResponseDto build() {
            return new SeatResponseDto(id, seatNumber, showId, status, version, lockedAt);
        }
    }
}
