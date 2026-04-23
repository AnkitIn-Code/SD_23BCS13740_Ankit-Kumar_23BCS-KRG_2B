package com.example.finalprojectspringboot.service;

import com.example.finalprojectspringboot.entity.Show;
import com.example.finalprojectspringboot.repository.ShowRepository;
import java.time.LocalDateTime;
import java.util.logging.Logger;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * This component initializes sample data on application startup
 * It creates 2 sample shows with 80 seats each (8 rows x 10 seats)
 */
@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger log = Logger.getLogger(DataInitializer.class.getName());
    private final ShowRepository showRepository;
    private final ShowService showService;

    public DataInitializer(ShowRepository showRepository, ShowService showService) {
        this.showRepository = showRepository;
        this.showService = showService;
    }

    @Override
    public void run(String... args) throws Exception {
        // Only seed data if no shows exist
        if (showRepository.count() == 0) {
            log.info("Initializing sample data...");

            // Create Show 1
            Show show1 = Show.builder()
                    .movieName("Avengers: Endgame")
                    .showTime(LocalDateTime.now().plusHours(2))
                    .totalSeats(80)
                    .availableSeats(80)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();
            Show savedShow1 = showRepository.save(show1);
            showService.createSeatsForShow(savedShow1);
            log.info("Created Show 1: " + savedShow1.getMovieName() + " with 80 seats");

            // Create Show 2
            Show show2 = Show.builder()
                    .movieName("The Dark Knight")
                    .showTime(LocalDateTime.now().plusHours(5))
                    .totalSeats(80)
                    .availableSeats(80)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();
            Show savedShow2 = showRepository.save(show2);
            showService.createSeatsForShow(savedShow2);
            log.info("Created Show 2: " + savedShow2.getMovieName() + " with 80 seats");

            log.info("Data initialization completed!");
        } else {
            log.info("Sample data already exists. Skipping initialization.");
        }
    }
}
