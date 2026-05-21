package com.mathquest.mathquest_backend.java.repository;

import com.mathquest.mathquest_backend.java.domain.Game;
import com.mathquest.mathquest_backend.java.domain.GameStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GameRepository extends JpaRepository<Game, Long> {
    List<Game> findByStatus(GameStatus gameStatus);
}
