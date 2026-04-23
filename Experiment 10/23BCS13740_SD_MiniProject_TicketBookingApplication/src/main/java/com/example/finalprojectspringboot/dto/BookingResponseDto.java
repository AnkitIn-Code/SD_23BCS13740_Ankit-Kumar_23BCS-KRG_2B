package com.example.finalprojectspringboot.dto;

import com.example.finalprojectspringboot.entity.BookingStatus;
import java.time.LocalDateTime;

public class BookingResponseDto {

    private Long bookingId;
    private Long seatId;
    private Long showId;
    private Long userId;
    private BookingStatus status;
    private LocalDateTime bookingTime;
    private String message;

    public BookingResponseDto() {}

    public BookingResponseDto(Long bookingId, Long seatId, Long showId, Long userId, BookingStatus status, LocalDateTime bookingTime, String message) {
        this.bookingId = bookingId;
        this.seatId = seatId;
        this.showId = showId;
        this.userId = userId;
        this.status = status;
        this.bookingTime = bookingTime;
        this.message = message;
    }

    public Long getBookingId() { return bookingId; }
    public void setBookingId(Long bookingId) { this.bookingId = bookingId; }

    public Long getSeatId() { return seatId; }
    public void setSeatId(Long seatId) { this.seatId = seatId; }

    public Long getShowId() { return showId; }
    public void setShowId(Long showId) { this.showId = showId; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public BookingStatus getStatus() { return status; }
    public void setStatus(BookingStatus status) { this.status = status; }

    public LocalDateTime getBookingTime() { return bookingTime; }
    public void setBookingTime(LocalDateTime bookingTime) { this.bookingTime = bookingTime; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public static BookingResponseDtoBuilder builder() {
        return new BookingResponseDtoBuilder();
    }

    public static class BookingResponseDtoBuilder {
        private Long bookingId;
        private Long seatId;
        private Long showId;
        private Long userId;
        private BookingStatus status;
        private LocalDateTime bookingTime;
        private String message;

        public BookingResponseDtoBuilder bookingId(Long bookingId) { this.bookingId = bookingId; return this; }
        public BookingResponseDtoBuilder seatId(Long seatId) { this.seatId = seatId; return this; }
        public BookingResponseDtoBuilder showId(Long showId) { this.showId = showId; return this; }
        public BookingResponseDtoBuilder userId(Long userId) { this.userId = userId; return this; }
        public BookingResponseDtoBuilder status(BookingStatus status) { this.status = status; return this; }
        public BookingResponseDtoBuilder bookingTime(LocalDateTime bookingTime) { this.bookingTime = bookingTime; return this; }
        public BookingResponseDtoBuilder message(String message) { this.message = message; return this; }

        public BookingResponseDto build() {
            return new BookingResponseDto(bookingId, seatId, showId, userId, status, bookingTime, message);
        }
    }
}
