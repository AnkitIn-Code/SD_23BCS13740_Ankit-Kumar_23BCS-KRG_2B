package com.example.finalprojectspringboot.service;

import com.example.finalprojectspringboot.dto.BookingResponseDto;
import com.example.finalprojectspringboot.entity.BookingStatus;
import com.example.finalprojectspringboot.entity.Seat;
import com.example.finalprojectspringboot.exception.ConcurrencyFailureException;
import com.example.finalprojectspringboot.exception.LockAcquisitionException;
import com.example.finalprojectspringboot.exception.ResourceNotFoundException;
import com.example.finalprojectspringboot.repository.SeatRepository;
import jakarta.persistence.OptimisticLockException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Logger;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;

@Service
public class BookingService {

    private static final Logger log = Logger.getLogger(BookingService.class.getName());
    private static final int MAX_BOOKING_RETRIES = 3;
    private static final long RETRY_WAIT_MILLIS = 200L;

    private final SeatRepository seatRepository;
    private final BookingExecutionService bookingExecutionService;

    public BookingService(SeatRepository seatRepository, BookingExecutionService bookingExecutionService) {
        this.seatRepository = seatRepository;
        this.bookingExecutionService = bookingExecutionService;
    }

    public BookingResponseDto safeBookSeat(Long seatId, Long userId) {
        RuntimeException lastException = null;

        for (int attempt = 1; attempt <= MAX_BOOKING_RETRIES; attempt++) {
            try {
                return bookingExecutionService.bookSeat(seatId, userId);
            } catch (RuntimeException ex) {
                if (!(ex instanceof ObjectOptimisticLockingFailureException)
                        && !(ex instanceof OptimisticLockException)
                        && !(ex instanceof LockAcquisitionException)) {
                    throw ex;
                }
                lastException = ex;
                log.warning("Booking retry " + attempt + " failed for seat " + seatId + " and user " + userId + ": " + ex.getMessage());
                sleepQuietly(RETRY_WAIT_MILLIS);
            }
        }

        throw new ConcurrencyFailureException(
                "Unable to book seat after " + MAX_BOOKING_RETRIES + " attempts due to concurrent activity.",
                lastException);
    }

    public List<BookingResponseDto> simulateConcurrentBooking(Long seatId) {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        List<Callable<BookingResponseDto>> tasks = new ArrayList<>();

        for (long userId = 1; userId <= 50; userId++) {
            long simulatedUserId = userId;
            tasks.add(() -> {
                try {
                    return safeBookSeat(seatId, simulatedUserId);
                } catch (RuntimeException ex) {
                    log.info("Concurrent booking failed for user " + simulatedUserId + " on seat " + seatId + ": " + ex.getMessage());
                    Long showId = seatRepository.findById(seatId).map(Seat::getShowId).orElse(null);
                    return BookingResponseDto.builder()
                            .seatId(seatId)
                            .showId(showId)
                            .userId(simulatedUserId)
                            .status(BookingStatus.FAILED)
                            .bookingTime(LocalDateTime.now())
                            .message(ex.getMessage())
                            .build();
                }
            });
        }

        try {
            List<Future<BookingResponseDto>> futures = executorService.invokeAll(tasks);
            List<BookingResponseDto> responses = new ArrayList<>();
            for (Future<BookingResponseDto> future : futures) {
                try {
                    responses.add(future.get());
                } catch (ExecutionException ex) {
                    throw new ConcurrencyFailureException("Failed to execute concurrent booking simulation.", ex.getCause());
                }
            }
            return responses;
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            throw new ConcurrencyFailureException("Concurrent booking simulation interrupted.", ex);
        } finally {
            executorService.shutdown();
        }
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
