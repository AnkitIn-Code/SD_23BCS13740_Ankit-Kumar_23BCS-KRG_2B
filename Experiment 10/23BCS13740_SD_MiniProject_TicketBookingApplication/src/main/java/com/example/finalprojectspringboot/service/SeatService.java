package com.example.finalprojectspringboot.service;

import com.example.finalprojectspringboot.dto.SeatResponseDto;
import com.example.finalprojectspringboot.entity.Seat;
import com.example.finalprojectspringboot.entity.SeatStatus;
import com.example.finalprojectspringboot.repository.SeatRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SeatService {

    private static final Logger log = Logger.getLogger(SeatService.class.getName());
    private final SeatRepository seatRepository;

    public SeatService(SeatRepository seatRepository) {
        this.seatRepository = seatRepository;
    }

    public List<SeatResponseDto> getSeatsByShow(Long showId) {
        return seatRepository.findByShowIdOrderBySeatNumberAsc(showId)
                .stream()
                .map(this::toSeatResponse)
                .toList();
    }

    @Transactional
    @Scheduled(fixedRate = 60_000)
    public void releaseExpiredLockedSeats() {
        LocalDateTime expiryCutoff = LocalDateTime.now().minusMinutes(2);
        List<Seat> expiredSeats = seatRepository.findExpiredLockedSeats(SeatStatus.LOCKED, expiryCutoff);

        if (expiredSeats.isEmpty()) {
            return;
        }

        expiredSeats.forEach(seat -> {
            seat.setStatus(SeatStatus.AVAILABLE);
            seat.setLockedAt(null);
        });

        seatRepository.saveAll(expiredSeats);
        log.info("Released " + expiredSeats.size() + " expired locked seats.");
    }

    private SeatResponseDto toSeatResponse(Seat seat) {
        return SeatResponseDto.builder()
                .id(seat.getId())
                .seatNumber(seat.getSeatNumber())
                .showId(seat.getShowId())
                .status(seat.getStatus())
                .version(seat.getVersion())
                .lockedAt(seat.getLockedAt())
                .build();
    }
}
