package com.example.finalprojectspringboot.repository;

import com.example.finalprojectspringboot.entity.Show;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShowRepository extends JpaRepository<Show, Long> {
    List<Show> findAll();
}

