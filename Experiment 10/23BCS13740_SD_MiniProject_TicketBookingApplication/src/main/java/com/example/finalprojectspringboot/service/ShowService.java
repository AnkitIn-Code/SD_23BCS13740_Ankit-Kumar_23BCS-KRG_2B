package com.example.finalprojectspringboot.service;

import com.example.finalprojectspringboot.dto.ShowRequestDto;
import com.example.finalprojectspringboot.dto.ShowResponseDto;
import com.example.finalprojectspringboot.entity.Seat;
import com.example.finalprojectspringboot.entity.SeatStatus;
import com.example.finalprojectspringboot.entity.Show;
import com.example.finalprojectspringboot.repository.SeatRepository;
import com.example.finalprojectspringboot.repository.ShowRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ShowService {

    private static final Logger log = Logger.getLogger(ShowService.class.getName());
    private final ShowRepository showRepository;
    private final SeatRepository seatRepository;

    public ShowService(ShowRepository showRepository, SeatRepository seatRepository) {
        this.showRepository = showRepository;
        this.seatRepository = seatRepository;
    }

    @Transactional
    public ShowResponseDto createShow(ShowRequestDto requestDto) {
        Show show = Show.builder()
                .movieName(requestDto.getMovieName())
                .showTime(requestDto.getShowTime())
                .totalSeats(requestDto.getTotalSeats())
                .availableSeats(requestDto.getTotalSeats())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Show savedShow = showRepository.save(show);

        // Create seats for this show
        createSeatsForShow(savedShow);

        log.info("Created show with id " + savedShow.getId() + " and " + requestDto.getTotalSeats() + " total seats.");

        return toShowResponse(savedShow);
    }

    public List<ShowResponseDto> getAllShows() {
        return showRepository.findAll()
                .stream()
                .map(this::toShowResponse)
                .toList();
    }

    public ShowResponseDto getShowById(Long showId) {
        Show show = showRepository.findById(showId)
                .orElseThrow(() -> new RuntimeException("Show not found with id: " + showId));
        return toShowResponse(show);
    }

    @Transactional
    protected void createSeatsForShow(Show show) {
        List<Seat> seats = new ArrayList<>();

        // Generate seats: Row A-H, Seats 1-10
        String[] rows = {"A", "B", "C", "D", "E", "F", "G", "H"};

        for (int i = 0; i < show.getTotalSeats(); i++) {
            int rowIndex = i / 10;
            int seatNum = (i % 10) + 1;

            if (rowIndex < rows.length) {
                String seatNumber = rows[rowIndex] + seatNum;

                Seat seat = Seat.builder()
                        .seatNumber(seatNumber)
                        .showId(show.getId())
                        .status(SeatStatus.AVAILABLE)
                        .version(0)
                        .build();

                seats.add(seat);
            }
        }

        seatRepository.saveAll(seats);
    }

    private ShowResponseDto toShowResponse(Show show) {
        return ShowResponseDto.builder()
                .id(show.getId())
                .movieName(show.getMovieName())
                .showTime(show.getShowTime())
                .totalSeats(show.getTotalSeats())
                .availableSeats(show.getAvailableSeats())
                .createdAt(show.getCreatedAt())
                .updatedAt(show.getUpdatedAt())
                .build();
    }
}
