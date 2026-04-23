package com.example.finalprojectspringboot.dto;

import jakarta.validation.constraints.NotNull;

public class BookingRequestDto {

    @NotNull
    private Long seatId;

    @NotNull
    private Long userId;

    public BookingRequestDto() {}

    public BookingRequestDto(Long seatId, Long userId) {
        this.seatId = seatId;
        this.userId = userId;
    }

    public Long getSeatId() { return seatId; }
    public void setSeatId(Long seatId) { this.seatId = seatId; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public static BookingRequestDtoBuilder builder() {
        return new BookingRequestDtoBuilder();
    }

    public static class BookingRequestDtoBuilder {
        private Long seatId;
        private Long userId;

        public BookingRequestDtoBuilder seatId(Long seatId) { this.seatId = seatId; return this; }
        public BookingRequestDtoBuilder userId(Long userId) { this.userId = userId; return this; }

        public BookingRequestDto build() {
            return new BookingRequestDto(seatId, userId);
        }
    }
}
