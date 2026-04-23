package com.example.finalprojectspringboot.repository;

import com.example.finalprojectspringboot.entity.Seat;
import com.example.finalprojectspringboot.entity.SeatStatus;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SeatRepository extends JpaRepository<Seat, Long> {

    List<Seat> findByShowIdOrderBySeatNumberAsc(Long showId);

    @Query("select s from Seat s where s.status = :status and s.lockedAt < :expiryTime")
    List<Seat> findExpiredLockedSeats(@Param("status") SeatStatus status, @Param("expiryTime") LocalDateTime expiryTime);
}
