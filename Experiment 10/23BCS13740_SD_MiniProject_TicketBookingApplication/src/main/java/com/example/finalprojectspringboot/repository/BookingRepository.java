package com.example.finalprojectspringboot.repository;

import com.example.finalprojectspringboot.entity.Booking;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findBySeatId(Long seatId);
}
