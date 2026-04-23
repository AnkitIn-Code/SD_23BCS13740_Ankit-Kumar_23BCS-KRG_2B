package com.example.finalprojectspringboot.service;

import com.example.finalprojectspringboot.dto.BookingResponseDto;
import com.example.finalprojectspringboot.entity.Booking;
import com.example.finalprojectspringboot.entity.BookingStatus;
import com.example.finalprojectspringboot.entity.Seat;
import com.example.finalprojectspringboot.entity.SeatStatus;
import com.example.finalprojectspringboot.entity.Show;
import com.example.finalprojectspringboot.exception.ConcurrencyFailureException;
import com.example.finalprojectspringboot.exception.LockAcquisitionException;
import com.example.finalprojectspringboot.exception.ResourceNotFoundException;
import com.example.finalprojectspringboot.exception.SeatUnavailableException;
import com.example.finalprojectspringboot.repository.BookingRepository;
import com.example.finalprojectspringboot.repository.SeatRepository;
import com.example.finalprojectspringboot.repository.ShowRepository;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.logging.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Service
public class BookingExecutionService {

    private static final Logger log = Logger.getLogger(BookingExecutionService.class.getName());
    private static final int MAX_LOCK_RETRIES = 3;
    private static final long RETRY_WAIT_MILLIS = 200L;
    private static final long BOOKING_SIMULATION_DELAY_MILLIS = 2_000L;

    private final SeatRepository seatRepository;
    private final BookingRepository bookingRepository;
    private final RedisLockService redisLockService;
    private final ShowRepository showRepository;

    public BookingExecutionService(SeatRepository seatRepository, BookingRepository bookingRepository,
                                   RedisLockService redisLockService, ShowRepository showRepository) {
        this.seatRepository = seatRepository;
        this.bookingRepository = bookingRepository;
        this.redisLockService = redisLockService;
        this.showRepository = showRepository;
    }

    @Transactional
    public BookingResponseDto bookSeat(Long seatId, Long userId) {
        String requestId = UUID.randomUUID().toString();
        String lockKey = buildLockKey(seatId);

        acquireLockWithRetry(lockKey, requestId);
        registerLockReleaseAfterTransaction(lockKey, requestId);

        Seat seat = seatRepository.findById(seatId)
                .orElseThrow(() -> new ResourceNotFoundException("Seat not found for id " + seatId));

        if (seat.getStatus() != SeatStatus.AVAILABLE) {
            throw new SeatUnavailableException("Seat " + seatId + " is not available for booking.");
        }

        // Persist LOCKED before the simulated delay so the seat state is visible across requests.
        seat.setStatus(SeatStatus.LOCKED);
        seat.setLockedAt(LocalDateTime.now());
        seatRepository.saveAndFlush(seat);

        sleepQuietly(BOOKING_SIMULATION_DELAY_MILLIS);

         seat.setStatus(SeatStatus.BOOKED);
         seat.setLockedAt(null);
         Seat bookedSeat = seatRepository.saveAndFlush(seat);

         // Update show's available seats
         Show show = showRepository.findById(bookedSeat.getShowId())
                 .orElseThrow(() -> new ResourceNotFoundException("Show not found for id " + bookedSeat.getShowId()));
         show.setAvailableSeats(show.getAvailableSeats() - 1);
         showRepository.save(show);

         Booking booking = bookingRepository.save(Booking.builder()
                 .userId(userId)
                 .seatId(bookedSeat.getId())
                 .showId(bookedSeat.getShowId())
                 .status(BookingStatus.CONFIRMED)
                 .bookingTime(LocalDateTime.now())
                 .build());

        return BookingResponseDto.builder()
                .bookingId(booking.getId())
                .seatId(bookedSeat.getId())
                .showId(bookedSeat.getShowId())
                .userId(userId)
                .status(booking.getStatus())
                .bookingTime(booking.getBookingTime())
                .message("Seat booked successfully.")
                .build();
    }

    private void acquireLockWithRetry(String lockKey, String requestId) {
        for (int attempt = 1; attempt <= MAX_LOCK_RETRIES; attempt++) {
            if (redisLockService.tryAcquireLock(lockKey, requestId)) {
                return;
            }

            log.info("Lock acquisition attempt " + attempt + " failed for " + lockKey);
            sleepQuietly(RETRY_WAIT_MILLIS);
        }

        throw new LockAcquisitionException("Could not acquire Redis lock for seat after " + MAX_LOCK_RETRIES + " attempts.");
    }

    private void registerLockReleaseAfterTransaction(String lockKey, String requestId) {
        if (!TransactionSynchronizationManager.isActualTransactionActive()) {
            return;
        }

        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCompletion(int status) {
                boolean released = redisLockService.releaseLock(lockKey, requestId);
                log.info("Released Redis lock " + lockKey + " after transaction completion: " + released);
            }
        });
    }

    private String buildLockKey(Long seatId) {
        return "seat_lock_" + seatId;
    }

    private void sleepQuietly(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            throw new ConcurrencyFailureException("Thread interrupted while processing booking.", ex);
        }
    }
}
