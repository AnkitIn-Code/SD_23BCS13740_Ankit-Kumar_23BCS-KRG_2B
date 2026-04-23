package com.example.finalprojectspringboot.controller;

import com.example.finalprojectspringboot.dto.BookingRequestDto;
import com.example.finalprojectspringboot.dto.BookingResponseDto;
import com.example.finalprojectspringboot.dto.SeatResponseDto;
import com.example.finalprojectspringboot.dto.ShowRequestDto;
import com.example.finalprojectspringboot.dto.ShowResponseDto;
import com.example.finalprojectspringboot.service.BookingService;
import com.example.finalprojectspringboot.service.SeatService;
import com.example.finalprojectspringboot.service.ShowService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@Validated
public class BookingController {

    private final BookingService bookingService;
    private final SeatService seatService;
    private final ShowService showService;

    public BookingController(BookingService bookingService, SeatService seatService, ShowService showService) {
        this.bookingService = bookingService;
        this.seatService = seatService;
        this.showService = showService;
    }

    // =============== Show Management Endpoints ===============

    /**
     * Create a new show with specified total seats
     * Example: POST /api/shows with JSON body
     */
    @PostMapping("/shows")
    public ResponseEntity<ShowResponseDto> createShow(@Valid @RequestBody ShowRequestDto requestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(showService.createShow(requestDto));
    }

    /**
     * Get all available shows
     */
    @GetMapping("/shows")
    public ResponseEntity<List<ShowResponseDto>> getAllShows() {
        return ResponseEntity.ok(showService.getAllShows());
    }

    /**
     * Get a specific show by ID
     */
    @GetMapping("/shows/{showId}")
    public ResponseEntity<ShowResponseDto> getShowById(@PathVariable Long showId) {
        return ResponseEntity.ok(showService.getShowById(showId));
    }

    // =============== Seat Management Endpoints ===============

    /**
     * Get all seats for a show with their status
     */
    @GetMapping("/seats/{showId}")
    public ResponseEntity<List<SeatResponseDto>> getSeatsByShow(@PathVariable Long showId) {
        return ResponseEntity.ok(seatService.getSeatsByShow(showId));
    }

    // =============== Booking Endpoints ===============

    /**
     * Book a seat using query parameters
     * Example: POST /api/book?seatId=1&userId=100
     */
    @PostMapping("/book")
    public ResponseEntity<BookingResponseDto> bookSeat(@RequestParam Long seatId, @RequestParam Long userId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bookingService.safeBookSeat(seatId, userId));
    }

    /**
     * Book a seat using request body
     * Example: POST /api/book/json with JSON body
     */
    @PostMapping("/book/json")
    public ResponseEntity<BookingResponseDto> bookSeatWithJson(@Valid @RequestBody BookingRequestDto requestDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(bookingService.safeBookSeat(requestDto.getSeatId(), requestDto.getUserId()));
    }

    /**
     * Simulate concurrent booking - 50 users trying to book the same seat
     * Only 1 will succeed. Useful for testing concurrency control with Redis locks and optimistic locking.
     * Example: POST /api/book/simulate?seatId=1
     */
    @PostMapping("/book/simulate")
    public ResponseEntity<List<BookingResponseDto>> simulateConcurrentBooking(@RequestParam Long seatId) {
        return ResponseEntity.ok(bookingService.simulateConcurrentBooking(seatId));
    }
}
