package com.example.finalprojectspringboot.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "bookings")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Long seatId;

    @Column(nullable = false)
    private Long showId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private BookingStatus status;

    @Column(nullable = false)
    private LocalDateTime bookingTime;

    public Booking() {}

    public Booking(Long id, Long userId, Long seatId, Long showId, BookingStatus status, LocalDateTime bookingTime) {
        this.id = id;
        this.userId = userId;
        this.seatId = seatId;
        this.showId = showId;
        this.status = status;
        this.bookingTime = bookingTime;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public Long getSeatId() { return seatId; }
    public void setSeatId(Long seatId) { this.seatId = seatId; }

    public Long getShowId() { return showId; }
    public void setShowId(Long showId) { this.showId = showId; }

    public BookingStatus getStatus() { return status; }
    public void setStatus(BookingStatus status) { this.status = status; }

    public LocalDateTime getBookingTime() { return bookingTime; }
    public void setBookingTime(LocalDateTime bookingTime) { this.bookingTime = bookingTime; }

    public static BookingBuilder builder() {
        return new BookingBuilder();
    }

    public static class BookingBuilder {
        private Long id;
        private Long userId;
        private Long seatId;
        private Long showId;
        private BookingStatus status;
        private LocalDateTime bookingTime;

        public BookingBuilder id(Long id) { this.id = id; return this; }
        public BookingBuilder userId(Long userId) { this.userId = userId; return this; }
        public BookingBuilder seatId(Long seatId) { this.seatId = seatId; return this; }
        public BookingBuilder showId(Long showId) { this.showId = showId; return this; }
        public BookingBuilder status(BookingStatus status) { this.status = status; return this; }
        public BookingBuilder bookingTime(LocalDateTime bookingTime) { this.bookingTime = bookingTime; return this; }

        public Booking build() {
            return new Booking(id, userId, seatId, showId, status, bookingTime);
        }
    }
}
